package alledrogo.controller;

import alledrogo.data.entity.UserEntity;
import alledrogo.data.enums.UserRole;
import alledrogo.data.enums.UserStatus;
import alledrogo.io.request.AuthenticationRequest;
import alledrogo.io.response.MessageResponse;
import alledrogo.io.response.UserDataResponse;
import alledrogo.security.jwt.JwtUtils;
import alledrogo.service.UserService;

import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * Api endpoint class, provides /auth endpoint to authentication and CRUD user data.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "auth")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    @Value("${alledrogo.app.jwtExpirationMs}")
    private int jwtExpirationMs;
    @Value("${alledrogo.app.jwtRefreshExpirationMs}")
    private int jwtRefreshExpirationMs;
    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, UserService userService, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@CookieValue(name = "refreshToken") String refreshToken) {

        if (refreshToken == null || !jwtUtils.validateJwtToken(refreshToken)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid refresh token"));
        }

        String username;
        try {
            username = jwtUtils.getUserNameFromJwtToken(refreshToken);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Could not extract user from refresh token"));
        }

        String refreshedToken = jwtUtils.generateJwtTokenFromUsername(username);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, "token=" + refreshedToken + "; Path=/; HttpOnly; Secure; SameSite=Strict; Max-Age=" + jwtExpirationMs / 1000);
        return ResponseEntity.ok()
                .headers(headers)
                .body(new MessageResponse("Token refreshed successfully!"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody AuthenticationRequest authenticationRequest) throws AuthenticationException {

        String username = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtils.generateJwtToken(authentication);
        String refreshToken = jwtUtils.generateJwtRefreshToken(authentication);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, "token=" + token + "; Path=/; HttpOnly; Secure; SameSite=Strict; Max-Age=" + jwtExpirationMs / 1000);
        headers.add(HttpHeaders.SET_COOKIE, "refreshToken=" + refreshToken + "; Path=/; HttpOnly; Secure; SameSite=Strict; Max-Age=" + jwtRefreshExpirationMs / 1000);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new MessageResponse("User logged in successfully!"));
    }

    @PostMapping("/logout")
    @PreAuthorize("hasRole('USER') or hasRole('VIP_USER') or hasRole('ADMIN')")
    public ResponseEntity<?> logoutUser() {
        SecurityContextHolder.clearContext();

        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.SET_COOKIE, "token=" + "; Path=/; HttpOnly; Secure; SameSite=Strict; Max-Age=" + 0);
        headers.add(HttpHeaders.SET_COOKIE, "refreshToken=" + "; Path=/; HttpOnly; Secure; SameSite=Strict; Max-Age=" + 0);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new MessageResponse("User logged out successfully!"));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@Valid @RequestBody AuthenticationRequest authenticationRequest) {

        String username = authenticationRequest.getUsername();
        String email = authenticationRequest.getEmail();

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(authenticationRequest.getPassword()) || StringUtils.isEmpty(email)) {
            return ResponseEntity.badRequest().body(new MessageResponse("All fields are required"));
        }

        if (!authenticationRequest.getPassword().equals(authenticationRequest.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Passwords do not match"));
        }

        if (userService.existsByEmail(email)) {
            return ResponseEntity.badRequest().body(new MessageResponse("An account with the given Email address already exists!"));
        }

        if (userService.existsByUsername(username)) {
            return ResponseEntity.badRequest().body(new MessageResponse("An account with the given Username already exists"));
        }

        UserEntity userEntity = new UserEntity(username,
                passwordEncoder.encode(authenticationRequest.getPassword()),
                UserRole.ROLE_USER,
                UserStatus.STATUS_ACTIVE,
                email);

        boolean createUser = userService.createUser(userEntity);

        if (createUser){
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, authenticationRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtUtils.generateJwtToken(authentication);
            String refreshToken = jwtUtils.generateJwtRefreshToken(authentication);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, "token=" + token + "; Path=/; HttpOnly; Secure; SameSite=Strict; Max-Age=" + jwtExpirationMs / 1000);
            headers.add(HttpHeaders.SET_COOKIE, "refreshToken=" + refreshToken + "; Path=/; HttpOnly; Secure; SameSite=Strict; Max-Age=" + jwtRefreshExpirationMs / 1000);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new MessageResponse("User registered successfully!"));
        }else{
            return ResponseEntity.badRequest().body((new MessageResponse("Something went wrong!")));
        }
    }

    @GetMapping("/userStatus")
    @PreAuthorize("hasRole('USER') or hasRole('VIP_USER') or hasRole('ADMIN')")
    public ResponseEntity<?> userStatus(@CookieValue(name = "token") String token) {
        UserEntity userEntity = userService.findByUsername(jwtUtils.getUserNameFromJwtToken(token));

        HttpHeaders headers = new HttpHeaders();

        headers.add("Set-Cookie", "username=" + userEntity.getUsername() + "; Path=/; Secure; SameSite=None; Max-Age=" + jwtExpirationMs / 1000);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new MessageResponse("User data received successfully!"));
    }

    @PostMapping("/userData")
    @PreAuthorize("hasRole('USER') or hasRole('VIP_USER') or hasRole('ADMIN')")
    public ResponseEntity<?> userData(@CookieValue(name = "token") String token) {
        UserEntity userEntity = userService.findByUsername(jwtUtils.getUserNameFromJwtToken(token));

        UserDataResponse userDataResponse = new UserDataResponse(userEntity.getUsername(), userEntity.getEmail());

        return ResponseEntity.ok()
                .body(userDataResponse);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('USER') or hasRole('VIP_USER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@RequestBody AuthenticationRequest authenticationRequest, @CookieValue(name = "token") String token) {

        String usernameFromToken = jwtUtils.getUserNameFromJwtToken(token);
        String usernameFromRequest = authenticationRequest.getUsername();

        if (!usernameFromToken.equals(usernameFromRequest)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Provided data does not match!"));
        }

        UserEntity userEntity = userService.findByUsername(usernameFromToken);

        if (userEntity == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("User does not exist!"));
        }

        userEntity.setAccountStatus(UserStatus.STATUS_DELETED);
        userService.updateUser(userEntity);

        if (userEntity.getAccountStatus() != UserStatus.STATUS_DELETED) {
            return ResponseEntity.badRequest().body(new MessageResponse("Failed to delete user account!"));
        }

        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.SET_COOKIE, "token=" + "; Path=/; HttpOnly; Secure; SameSite=Strict; Max-Age=" + 0);
        headers.add(HttpHeaders.SET_COOKIE, "refreshToken=" + "; Path=/; HttpOnly; Secure; SameSite=Strict; Max-Age=" + 0);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new MessageResponse("User account has been deleted!"));
    }

    @PutMapping("/changePassword")
    @PreAuthorize("hasRole('USER') or hasRole('VIP_USER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@Valid @RequestBody AuthenticationRequest authenticationRequest,  @CookieValue(name = "token") String token) throws Exception{

        String tokenUsername = jwtUtils.getUserNameFromJwtToken(token);

        UserEntity userEntity = userService.findByUsername(tokenUsername);

        if (userEntity == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("User does not exist!"));
        }

        if (!passwordEncoder.matches(authenticationRequest.getPassword(), userEntity.getPassword())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Incorrect current password!"));
        }

        if (!authenticationRequest.getNewPassword().equals(authenticationRequest.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(new MessageResponse("New and confirmation password do not match!"));
        }

        userEntity.setPassword(passwordEncoder.encode(authenticationRequest.getNewPassword()));

        boolean isUpdateSuccessful = userService.updateUser(userEntity);

        if (isUpdateSuccessful) {
            return ResponseEntity.ok(new MessageResponse("User password has been updated!"));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Failed to update user password!"));
        }
    }
}


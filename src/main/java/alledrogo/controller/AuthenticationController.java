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
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Api endpoint class, provides /auth endpoint to provide basic authentication and manipulate user data.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/auth")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, UserService userService, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody AuthenticationRequest authenticationRequest) {

        String username = authenticationRequest.getUsername();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, authenticationRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtils.generateJwtToken(authentication);

        HttpHeaders headers = new HttpHeaders();

        headers.add("Set-Cookie", "token=" + token + "; Path=/; HttpOnly; Secure; SameSite=None; Expires=" + getExpirationTimeString());

        return ResponseEntity.ok()
                .headers(headers)
                .body(new MessageResponse("User logged in successfully!"));
    }

    @PostMapping("/logout")
    @PreAuthorize("hasRole('USER') or hasRole('VIP_USER') or hasRole('ADMIN')")
    public ResponseEntity<?> logoutUser() {
        SecurityContextHolder.clearContext();

        HttpHeaders headers = new HttpHeaders();

        headers.add("Set-Cookie", "token=" + "; Path=/; HttpOnly; Secure; SameSite=None; Expires=" + expireTokens());

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

        if (userService.existsByUsername(username)) {
            return ResponseEntity.badRequest().body(new MessageResponse("An account with the given Username already exists"));
        }

        if (userService.existsByEmail(email)) {
            return ResponseEntity.badRequest().body(new MessageResponse("An account with the given Email address already exists!"));
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

            HttpHeaders headers = new HttpHeaders();

            headers.add("Set-Cookie", "token=" + token + "; Path=/; HttpOnly; Secure; SameSite=None; Expires=" + getExpirationTimeString());

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
        try {
            UserEntity userEntity = userService.findByUsername(jwtUtils.getUserNameFromJwtToken(token));

            HttpHeaders headers = new HttpHeaders();

            headers.add("Set-Cookie", "username=" + userEntity.getUsername() + "; Path=/; Secure; SameSite=None; Expires=" + getExpirationTimeString());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new MessageResponse("User data received successfully!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("User does not exist!"));
        }
    }

    @PostMapping("/userData")
    @PreAuthorize("hasRole('USER') or hasRole('VIP_USER') or hasRole('ADMIN')")
    public ResponseEntity<?> userData(@CookieValue(name = "token") String token) {
        try {
            UserEntity userEntity = userService.findByUsername(jwtUtils.getUserNameFromJwtToken(token));

            UserDataResponse userDataResponse = new UserDataResponse(userEntity.getUsername(), userEntity.getEmail());

            return ResponseEntity.ok()
                    .body(userDataResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("User does not exist!"));
        }
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
        return ResponseEntity.ok(new MessageResponse("User account has been deleted!"));
    }

    @PutMapping("/changePassword")
    @PreAuthorize("hasRole('USER') or hasRole('VIP_USER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@Valid @RequestBody AuthenticationRequest authenticationRequest,  @CookieValue(name = "token") String token) {

        String tokenUsername = jwtUtils.getUserNameFromJwtToken(token);

        UserEntity userEntity = userService.findByUsername(tokenUsername);

        if (userEntity == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("User does not exist!"));
        }

        if (!passwordEncoder.matches(authenticationRequest.getPassword(), userEntity.getPassword())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Incorrect current password!"));
        }

        if (!authenticationRequest.getNewPassword().equals(authenticationRequest.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(new MessageResponse("New password and confirmation do not match!"));
        }

        String currentEncryptedPassword = userEntity.getPassword();

        userEntity.setPassword(passwordEncoder.encode(authenticationRequest.getNewPassword()));
        userService.updateUser(userEntity);

        UserEntity updatedUserEntity = userService.findByUsername(tokenUsername);

        if (!currentEncryptedPassword.equals(updatedUserEntity.getPassword())) {
            return ResponseEntity.ok(new MessageResponse("User password has been updated!"));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Failed to update user password!"));
        }
    }

    private String getExpirationTimeString() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 10);
        Date expirationDate = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        return sdf.format(expirationDate);
    }

    private String expireTokens() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date expirationDate = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        return sdf.format(expirationDate);
    }
}


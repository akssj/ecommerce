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
    public ResponseEntity<?> userData(@CookieValue(name = "token") String token) {
        try {
            UserEntity userEntity = userService.findByUsername(jwtUtils.getUserNameFromJwtToken(token));

            UserDataResponse userDataResponse = new UserDataResponse(userEntity.getUsername(), userEntity.getEmail());

            HttpHeaders headers = new HttpHeaders();

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(userDataResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("User does not exist!"));
        }
    }


    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteUser(@Valid @RequestBody AuthenticationRequest authenticationRequest, @CookieValue(name = "token") String token) {

        String requestUsername = authenticationRequest.getUsername();
        String tokenUsername = jwtUtils.getUserNameFromJwtToken(token);

        if (!requestUsername.equals(tokenUsername)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Something went wrong!."));
        }

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserEntity userEntity = userService.findByUsername(requestUsername);

        boolean deleteUser = userService.deleteUser(userEntity);
        if (deleteUser){
            return ResponseEntity.ok(new MessageResponse("User deleted successfully!"));
        }else{
            return ResponseEntity.badRequest().body(new MessageResponse("Something went wrong!."));
        }
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody AuthenticationRequest authenticationRequest,  @CookieValue(name = "token") String token) {

        String requestUsername = authenticationRequest.getUsername();
        String tokenUsername = jwtUtils.getUserNameFromJwtToken(token);

        if (!requestUsername.equals(tokenUsername)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Something went wrong!."));
        }

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(requestUsername, authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserEntity userEntity = userService.findByUsername(requestUsername);

        //TODO updating user

        boolean updateUser = userService.updateUser(userEntity);
        if (updateUser){
            return ResponseEntity.ok(new MessageResponse("User data updated!"));
        }else{
            return ResponseEntity.badRequest().body(new MessageResponse("Something went wrong!"));
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


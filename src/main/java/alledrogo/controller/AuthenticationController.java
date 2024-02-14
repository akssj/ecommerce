package alledrogo.controller;

import alledrogo.data.entity.UserEntity;
import alledrogo.io.request.AuthenticationRequest;
import alledrogo.io.response.MessageResponse;
import alledrogo.security.jwt.JwtUtils;
import alledrogo.service.UserService;

import jakarta.validation.Valid;
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

        String expirationTimeString = getExpirationTimeString();

        headers.add("Set-Cookie", "token=" + token + "; Path=/; HttpOnly; Secure; SameSite=None; Expires=" + expirationTimeString);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new MessageResponse("User logged in successfully!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        SecurityContextHolder.clearContext();

        HttpHeaders headers = new HttpHeaders();

        String expirationTimeString = expireTokens();

        headers.add("Set-Cookie", "token=" + "; Path=/; HttpOnly; Secure; SameSite=None; Expires=" + expirationTimeString);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new MessageResponse("User logged out successfully!"));
    }


    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@Valid @RequestBody AuthenticationRequest authenticationRequest) {

        String username = authenticationRequest.getUsername();

        if (userService.existsByUsername(username)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Username is already taken!"));
        }

        UserEntity userEntity = new UserEntity(username, passwordEncoder.encode(authenticationRequest.getPassword()));
        boolean createUser = userService.createUser(userEntity);

        if (createUser){
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, authenticationRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtUtils.generateJwtToken(authentication);

            HttpHeaders headers = new HttpHeaders();

            String expirationTimeString = getExpirationTimeString();

            headers.add("Set-Cookie", "token=" + token + "; Path=/; HttpOnly; Secure; SameSite=None; Expires=" + expirationTimeString);

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

            String expirationTimeString = getExpirationTimeString();

            headers.add("Set-Cookie", "userId=" + userEntity.getId() + "; Path=/; Secure; SameSite=None; Expires=" + expirationTimeString);
            headers.add("Set-Cookie", "username=" + userEntity.getUsername() + "; Path=/; Secure; SameSite=None; Expires=" + expirationTimeString);
            headers.add("Set-Cookie", "role=" + userEntity.getRole() + "; Path=/; Secure; SameSite=None; Expires=" + expirationTimeString);
            headers.add("Set-Cookie", "balance=" + userEntity.getBalance() + "; Path=/; Secure; SameSite=None; Expires=" + expirationTimeString);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new MessageResponse("User data received successfully!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("User does not exist!"));
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteUser(@Valid @RequestBody AuthenticationRequest authenticationRequest) {

        String username = authenticationRequest.getUsername();

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserEntity userEntity = userService.findByUsername(username);

        boolean deleteUser = userService.deleteUser(userEntity);
        if (deleteUser){
            return ResponseEntity.ok(new MessageResponse("User deleted successfully!"));
        }else{
            return ResponseEntity.badRequest().body(new MessageResponse("Something went wrong!."));
        }
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody AuthenticationRequest authenticationRequest) {

        String username = authenticationRequest.getUsername();

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserEntity userEntity = userService.findByUsername(username);

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
        Date expirationDate = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        return sdf.format(expirationDate);
    }
}


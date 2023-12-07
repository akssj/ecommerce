package main.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import main.io.request.LoginRequest;
import main.io.request.SignupRequest;
import main.io.response.JwtResponse;
import main.io.response.MessageResponse;
import main.io.response.UserStatusResponse;
import main.data.entity.UserEntity;
import main.security.services.UserDetailsImpl;
import main.security.jwt.JwtUtils;

import main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/auth", method = RequestMethod.POST, consumes="application/json" )
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
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest) {

        if (!userService.existsByUsername(loginRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("User does not exist!"));
        }

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(token, userDetails.getId(), userDetails.getUsername(), roles, userDetails.getBalance()));
    }
    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@Valid @RequestBody SignupRequest signUpRequest) {

        if (userService.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Username is already taken!"));
        }else{
            UserEntity userEntity = new UserEntity(signUpRequest.getUsername(), passwordEncoder.encode(signUpRequest.getPassword()));
            userService.createUser(userEntity);

            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        }
    }

    //TODO update current user data on demand?
    @GetMapping("/userStatus")
    public ResponseEntity<?> updateUserStatus(@RequestHeader(name = "Authorization") String token) {

        Optional<UserEntity> userEntity = userService.findByUsername(jwtUtils.getUserNameFromJwtToken(token));

        if (userEntity.isPresent()) {
            UserEntity user = userEntity.get();
            return ResponseEntity.ok(new UserStatusResponse(user.getId(), user.getUsername(), user.getRole(), user.getBalance()));
        }else {
            return ResponseEntity.badRequest().body(new MessageResponse("User does not exist!"));
        }
    }
}

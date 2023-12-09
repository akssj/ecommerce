package main.controllers;

import jakarta.validation.Valid;

import main.io.request.AuthenticationRequest;
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

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = new UserDetailsImpl(userService.findByUsername(username));

        return ResponseEntity.ok(new JwtResponse(token, userDetails.getId(), username, userDetails.getAuthorities(), userDetails.getBalance()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@Valid @RequestBody AuthenticationRequest authenticationRequest) {

        String username = authenticationRequest.getUsername();

        if (userService.existsByUsername(username)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Username is already taken!"));
        }else{
            UserEntity userEntity = new UserEntity(username, passwordEncoder.encode(authenticationRequest.getPassword()));
            userService.createUser(userEntity);

            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        }
    }

    //TODO update current user data on demand with new token
    //TODO make use of {id}
    @GetMapping("/{id}/userStatus")
    public ResponseEntity<?> updateUserStatus(@RequestHeader(name = "Authorization") String token) {
        try {
            UserEntity userEntity = userService.findByUsername(jwtUtils.getUserNameFromJwtToken(token));
            return ResponseEntity.ok(new UserStatusResponse(userEntity.getId(), userEntity.getUsername(), userEntity.getRole(), userEntity.getBalance()));
        }catch(Exception e){
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

        try{
            userService.deleteUser(userEntity);
            //TODO delete all listed products and such
            return ResponseEntity.ok(new MessageResponse("User deleted successfully!"));
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Something went wrong!." + e));
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

        try{
            userService.updateUser(userEntity);
            return ResponseEntity.ok(new MessageResponse("User data updated!"));
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Something went wrong!." + e));
        }
    }

}

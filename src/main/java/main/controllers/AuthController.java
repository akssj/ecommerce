package main.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import main.dto.request.UserStatusRequest;
import main.dto.request.LoginRequest;
import main.dto.request.SignupRequest;
import main.dto.response.JwtResponse;
import main.dto.response.MessageResponse;
import main.dto.response.UserStatusResponse;
import main.entity.UserEntity;
import main.repository.UserRepository;
import main.security.services.UserDetailsImpl;
import main.security.jwt.JwtUtils;

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
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        if (!userRepository.existsByUsername(loginRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("User does not exist!"));
        }

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(token, userDetails.getId(), userDetails.getUsername(), roles, userDetails.getBalance()));
    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Username is already taken!"));
        }else{
            UserEntity user = new UserEntity(signUpRequest.getUsername(), passwordEncoder.encode(signUpRequest.getPassword()));
            userRepository.save(user);

            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        }
    }
    @PostMapping("/userStatus")
    public ResponseEntity<?> authenticateLoginStatus(@RequestBody UserStatusRequest userStatusRequest) {

        if (!userRepository.existsByUsername(userStatusRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("User does not exist!"));
        }

        Optional<UserEntity> userEntity = userRepository.findByUsername(userStatusRequest.getUsername());

        UserEntity user = userEntity.get();

        return ResponseEntity.ok(new UserStatusResponse(user.getId(), user.getUsername(), user.getRole(), user.getBalance()));
    }
}

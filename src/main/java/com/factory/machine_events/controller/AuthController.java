package com.factory.machine_events.controller;

import com.factory.machine_events.dto.LoginRequest;
import com.factory.machine_events.dto.LoginResponse;
import com.factory.machine_events.dto.RegisterRequest;
import com.factory.machine_events.dto.UserResponse;
import com.factory.machine_events.entity.User;
import com.factory.machine_events.repository.UserRepository;
import com.factory.machine_events.security.JwtUtils;
import com.factory.machine_events.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        return new ResponseEntity<>(userService.register(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {

        try {
            User user = userService.validateUser(loginRequest);

            String jwtToken = jwtUtils.generateToken(user.getId(),user.getRole());

            return ResponseEntity.ok(new LoginResponse(jwtToken,userService.mapToResponse(user)));
        }
        catch (AuthenticationException e) {
            System.out.println("Authentication Failed, info : " + e.getMessage());
            return ResponseEntity.status(401).build();
        }
    }
}

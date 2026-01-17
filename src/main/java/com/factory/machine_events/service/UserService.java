package com.factory.machine_events.service;

import com.factory.machine_events.dto.LoginRequest;
import com.factory.machine_events.dto.RegisterRequest;
import com.factory.machine_events.dto.UserResponse;
import com.factory.machine_events.entity.User;
import com.factory.machine_events.entity.UserRole;
import com.factory.machine_events.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserResponse mapToResponse(User newUser) {
        return UserResponse.builder()
                .id(newUser.getId())
                .email(newUser.getEmail())
                .password(newUser.getPassword())
                .build();
    }

    public UserResponse register(RegisterRequest request) {
        UserRole role = request.getRole() != null
                ? request.getRole()
                : UserRole.USER;
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        User newUser = userRepository.save(user);

        return mapToResponse(newUser);
    }

    public User validateUser(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());
        if (user == null) {
            throw new RuntimeException("Invalid Credentials");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid Credentials");
        }

        return user;
    }
}

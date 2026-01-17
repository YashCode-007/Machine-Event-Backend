package com.factory.machine_events.dto;

import com.factory.machine_events.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @Email(message = "Email is required")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    private UserRole role;
}

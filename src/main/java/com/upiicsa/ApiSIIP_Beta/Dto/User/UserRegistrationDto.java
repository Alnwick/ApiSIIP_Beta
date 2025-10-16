package com.upiicsa.ApiSIIP_Beta.Dto.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegistrationDto(
        @NotBlank @Email
        String email,

        @NotBlank @Size(min = 8)
        String password,

        @NotBlank @Size(min = 8)
        String confirmPassword,

        @NotBlank
        String name,

        @NotBlank
        String fatherLastName,

        @NotBlank
        String motherLastName
) {
}

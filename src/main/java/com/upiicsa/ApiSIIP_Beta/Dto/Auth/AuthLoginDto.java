package com.upiicsa.ApiSIIP_Beta.Dto.Auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthLoginDto(
        @NotBlank @Email
        String email,
        @NotBlank
        String password
) {
}

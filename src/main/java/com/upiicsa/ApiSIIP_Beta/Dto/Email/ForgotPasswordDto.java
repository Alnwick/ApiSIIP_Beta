package com.upiicsa.ApiSIIP_Beta.Dto.Email;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordDto(
        @NotBlank @Email
        String email
) {
}

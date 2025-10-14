package com.upiicsa.ApiSIIP_Beta.Dto.Email;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordDto(
        @NotBlank
        String token,
        @NotBlank @Size(min = 8)
        String newPassword
) {
}

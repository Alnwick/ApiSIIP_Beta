package com.upiicsa.ApiSIIP_Beta.Dto.Email;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EmailConfirmationDto(
        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 6, max = 6)
        String code
) {
}

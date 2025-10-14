package com.upiicsa.ApiSIIP_Beta.Dto.Auth;

public record AuthResponseDto(
        String message,
        String jwtToken,
        boolean flag
) {
}

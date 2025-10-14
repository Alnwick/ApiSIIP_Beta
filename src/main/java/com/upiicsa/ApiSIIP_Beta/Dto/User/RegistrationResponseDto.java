package com.upiicsa.ApiSIIP_Beta.Dto.User;

import com.upiicsa.ApiSIIP_Beta.Model.UserSIIP;

public record RegistrationResponseDto(
        String message,
        String email,
        String fatherLastName
) {
    public RegistrationResponseDto(UserSIIP user, String message){
        this(message, user.getEmail(),user.getFatherLastName());
    }
}

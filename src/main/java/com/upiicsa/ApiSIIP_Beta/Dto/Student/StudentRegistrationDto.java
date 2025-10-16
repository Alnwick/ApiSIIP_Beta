package com.upiicsa.ApiSIIP_Beta.Dto.Student;

public record StudentRegistrationDto(
        String email,
        String fatherLastName,
        String motherLastName,
        String name,
        String password,
        String confirmPassword,
        String enrollment,
        String phone,
        String school,
        String career
) {
}

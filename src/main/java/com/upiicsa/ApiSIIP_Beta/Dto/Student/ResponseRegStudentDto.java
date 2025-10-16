package com.upiicsa.ApiSIIP_Beta.Dto.Student;

import com.upiicsa.ApiSIIP_Beta.Model.Student;

public record ResponseRegStudentDto(
        String fatherLastName,
        String email,
        String enrollment
) {
    public ResponseRegStudentDto(Student student) {
        this(student.getFatherLastName(), student.getEmail(), student.getEnrollment());
    }
}

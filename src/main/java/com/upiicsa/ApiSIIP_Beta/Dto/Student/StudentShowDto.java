package com.upiicsa.ApiSIIP_Beta.Dto.Student;

import com.upiicsa.ApiSIIP_Beta.Model.Student;

public record StudentShowDto(
        Long id,
        String fullName,
        String enrollment,
        String docState
) {
    public StudentShowDto(Student student) {
        this(student.getId(), student.getFatherLastName() + student.getMotherLastName() + student.getName(),
                student.getEnrollment(), student.getDocumentation().getState().toString());
    }
}

package com.upiicsa.ApiSIIP_Beta.Controller;

import com.upiicsa.ApiSIIP_Beta.Dto.Email.EmailConfirmationDto;
import com.upiicsa.ApiSIIP_Beta.Dto.Student.ResponseRegStudentDto;
import com.upiicsa.ApiSIIP_Beta.Dto.Student.StudentRegistrationDto;
import com.upiicsa.ApiSIIP_Beta.Model.Student;
import com.upiicsa.ApiSIIP_Beta.Service.EmailVerificationService;
import com.upiicsa.ApiSIIP_Beta.Service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private EmailVerificationService verificationService;

    @PostMapping("/register")
    public ResponseEntity<ResponseRegStudentDto> registerUser(@RequestBody @Valid StudentRegistrationDto registrationDto) {

        Student student = studentService.registerStudent(registrationDto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(student.getId()).toUri();

        return ResponseEntity.created(location).body(new ResponseRegStudentDto(student));
    }

    @PostMapping("/confirm-email")
    public ResponseEntity<Void> confirmEmail(@RequestBody @Valid EmailConfirmationDto emailConfirmation) {
        try{
            verificationService.confirmEmail(emailConfirmation);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(null);
        }
    }
}

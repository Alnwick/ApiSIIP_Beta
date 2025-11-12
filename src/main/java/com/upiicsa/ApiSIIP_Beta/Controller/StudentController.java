package com.upiicsa.ApiSIIP_Beta.Controller;

import com.upiicsa.ApiSIIP_Beta.Dto.Document.DocumentShowForStudentDto;
import com.upiicsa.ApiSIIP_Beta.Dto.Document.UploadDocDto;
import com.upiicsa.ApiSIIP_Beta.Dto.Email.EmailConfirmationDto;
import com.upiicsa.ApiSIIP_Beta.Dto.Student.ResponseRegStudentDto;
import com.upiicsa.ApiSIIP_Beta.Dto.Student.StudentRegistrationDto;
import com.upiicsa.ApiSIIP_Beta.Model.Student;
import com.upiicsa.ApiSIIP_Beta.Service.DocumentService;
import com.upiicsa.ApiSIIP_Beta.Service.DocumentationService;
import com.upiicsa.ApiSIIP_Beta.Service.EmailVerificationService;
import com.upiicsa.ApiSIIP_Beta.Service.StudentService;
import com.upiicsa.ApiSIIP_Beta.Utils.AuthHelper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/students")
@SecurityRequirement(name = "bearerAuth")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private EmailVerificationService verificationService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private DocumentationService documentationService;

    @GetMapping("/view-my-documentation")
    @PreAuthorize("hasAnyRole('STUDENT')")
    public ResponseEntity<List<DocumentShowForStudentDto>> viewMyDocumentation() {
        Long idStudent = AuthHelper.getAuthenticatedUserId();

        List<DocumentShowForStudentDto> documentation = documentationService.getDocumentation(idStudent)
                .stream()
                .map(d -> new DocumentShowForStudentDto(d))
                .collect(Collectors.toList());

        return ResponseEntity.ok(documentation);
    }

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
        verificationService.confirmEmail(emailConfirmation);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/upload-document")
    @PreAuthorize("hasAnyRole('STUDENT')")
    public ResponseEntity<String> uploadDocument (@ModelAttribute UploadDocDto uploadDoc){
        MultipartFile file = uploadDoc.file();
        String typeDocument = uploadDoc.typeDocument();
        Long idUser = AuthHelper.getAuthenticatedUserId();

        if(file.isEmpty()){
            return ResponseEntity.badRequest().body("Please select a file");
        }

        try {
            String fileUrl = documentService.saveFile(file, typeDocument, idUser);
            return ResponseEntity.ok("Archive saved successfully. URL: " + fileUrl);
        } catch (IOException e){
            return ResponseEntity.internalServerError().body("Error saving file: " + e.getMessage());
        }
    }
}

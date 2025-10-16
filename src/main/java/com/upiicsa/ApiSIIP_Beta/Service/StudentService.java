package com.upiicsa.ApiSIIP_Beta.Service;

import com.upiicsa.ApiSIIP_Beta.Dto.Student.StudentRegistrationDto;
import com.upiicsa.ApiSIIP_Beta.Model.Document;
import com.upiicsa.ApiSIIP_Beta.Model.Documentation;
import com.upiicsa.ApiSIIP_Beta.Model.Enum.Career;
import com.upiicsa.ApiSIIP_Beta.Model.Enum.DocumentType;
import com.upiicsa.ApiSIIP_Beta.Model.Enum.School;
import com.upiicsa.ApiSIIP_Beta.Model.Role;
import com.upiicsa.ApiSIIP_Beta.Model.Student;
import com.upiicsa.ApiSIIP_Beta.Repository.RoleRepository;
import com.upiicsa.ApiSIIP_Beta.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailVerificationService  verificationService;

    @Autowired
    private DocumentationService documentationService;

    @Autowired
    private DocumentService  documentService;

    private final List<DocumentType> requiredDocumentTypes = List.of(
            DocumentType.CED_REGISTRO,
            DocumentType.CAP_SIBOLTRA,
            DocumentType.CONS_VIGENCIA,
            DocumentType.COP_HORARIO,
            DocumentType.IMP_REGISTRO
    );


    @Transactional
    public Student registerStudent(StudentRegistrationDto registrationDto) {
        if (!registrationDto.password().equals(registrationDto.confirmPassword()))
            throw new IllegalArgumentException("Invalid password");

        Role defaultRole = roleRepository.findByRoleName("STUDENT")
                .orElseThrow(() -> new RuntimeException("Default Role Not Found"));

        Student newStudent = Student.builder()
                .email(registrationDto.email())
                .password(passwordEncoder.encode(registrationDto.password()))
                .fatherLastName(registrationDto.fatherLastName()).motherLastName(registrationDto.motherLastName())
                .name(registrationDto.name())
                .enabled(false).accountNonExpired(true).accountNonLocked(true).credentialsNonExpired(true)
                .rolesList(Set.of(defaultRole))
                .enrollment(registrationDto.enrollment())
                .phone(registrationDto.phone())
                .school(School.valueOf(registrationDto.school())).career(Career.valueOf(registrationDto.career()))
                .build();

        studentRepository.save(newStudent);
        verificationService.createAndSendConfirmationCode(newStudent);

        Documentation documentation = documentationService.createInitialDocumentation(newStudent);
        List<Document> documents = new ArrayList<>();

        for (DocumentType type : requiredDocumentTypes) {
            Document document = documentService.createInitialDocument(documentation, type);
            documents.add(document);
        }

        documentation.setDocuments(documents);

        newStudent.setDocumentation(documentation);

        return newStudent;
    }
}

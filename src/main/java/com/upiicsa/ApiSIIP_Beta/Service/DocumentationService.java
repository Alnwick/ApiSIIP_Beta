package com.upiicsa.ApiSIIP_Beta.Service;

import com.upiicsa.ApiSIIP_Beta.Model.Document;
import com.upiicsa.ApiSIIP_Beta.Model.Documentation;
import com.upiicsa.ApiSIIP_Beta.Model.Enum.StateDocumentation;
import com.upiicsa.ApiSIIP_Beta.Model.Student;
import com.upiicsa.ApiSIIP_Beta.Repository.DocumentationRepository;
import com.upiicsa.ApiSIIP_Beta.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DocumentationService {

    @Autowired
    private DocumentationRepository documentationRepository;

    @Autowired
    private StudentRepository studentRepository;

    public Documentation createInitialDocumentation(Student student) {
        Documentation documentation = new Documentation();
        documentation.setStudent(student);
        documentation.setCreatedDate(LocalDateTime.now());
        documentation.setState(StateDocumentation.INCOMPLETA);
        return documentation;
    }

    public List<Document> getDocumentation(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Documentation documentation = documentationRepository.findById(student.getDocumentation().getId())
                .orElseThrow(() -> new UsernameNotFoundException("Documentation not found"));

        return documentation.getDocuments();
    }
}

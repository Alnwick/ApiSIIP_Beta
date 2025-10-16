package com.upiicsa.ApiSIIP_Beta.Service;

import com.upiicsa.ApiSIIP_Beta.Model.Documentation;
import com.upiicsa.ApiSIIP_Beta.Model.Enum.StateDocumentation;
import com.upiicsa.ApiSIIP_Beta.Model.Student;
import com.upiicsa.ApiSIIP_Beta.Repository.DocumentationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DocumentationService {

    @Autowired
    private DocumentationRepository documentationRepository;

    public Documentation createInitialDocumentation(Student student) {
        Documentation documentation = new Documentation();
        documentation.setStudent(student);
        documentation.setCreatedDate(LocalDateTime.now());
        documentation.setState(StateDocumentation.INCOMPLETA);
        return documentation;
    }
}

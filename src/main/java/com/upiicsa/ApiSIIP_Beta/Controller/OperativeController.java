package com.upiicsa.ApiSIIP_Beta.Controller;

import com.upiicsa.ApiSIIP_Beta.Dto.Document.DocumentShowDto;
import com.upiicsa.ApiSIIP_Beta.Dto.Student.StudentShowDto;
import com.upiicsa.ApiSIIP_Beta.Service.DocumentationService;
import com.upiicsa.ApiSIIP_Beta.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/operatives")
public class OperativeController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private DocumentationService documentationService;

    @GetMapping("/get-allStudents")
    @PreAuthorize("hasAnyRole('OPERATIVE', 'ADMIN')")
    public ResponseEntity<Page<StudentShowDto>> getOperatives(@PageableDefault(size = 2) Pageable pageable) {

        Page<StudentShowDto> students =  studentService.getStudents(pageable);

        return ResponseEntity.ok(students);
    }

    @GetMapping("/get-documentationForStudent/{id}")
    @PreAuthorize("hasAnyRole('OPERATIVE', 'ADMIN')")
    public ResponseEntity<List<DocumentShowDto>> getDocumentationForStudent(@PathVariable Long id) {

        return ResponseEntity.ok(documentationService.getDocumentation(id));
    }
}

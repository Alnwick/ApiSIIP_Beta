package com.upiicsa.ApiSIIP_Beta.Controller;

import com.upiicsa.ApiSIIP_Beta.Dto.Document.CheckDocumentDto;
import com.upiicsa.ApiSIIP_Beta.Dto.Document.DocumentShowDto;
import com.upiicsa.ApiSIIP_Beta.Dto.Student.StudentShowDto;
import com.upiicsa.ApiSIIP_Beta.Service.DocumentService;
import com.upiicsa.ApiSIIP_Beta.Service.DocumentationService;
import com.upiicsa.ApiSIIP_Beta.Service.StudentService;
import com.upiicsa.ApiSIIP_Beta.Utils.AuthHelper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/operatives")
@SecurityRequirement(name = "bearerAuth")
public class OperativeController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private DocumentationService documentationService;

    @Autowired
    private DocumentService documentService;

    @GetMapping("/get-allStudents")
    @PreAuthorize("hasAnyRole('OPERATIVE', 'ADMIN')")
    public ResponseEntity<Page<StudentShowDto>> getStudents(@PageableDefault(size = 2) Pageable pageable) {

        Page<StudentShowDto> students =  studentService.getStudents(pageable);

        return ResponseEntity.ok(students);
    }

    @GetMapping("/get-documentationForStudent/{id}")
    @PreAuthorize("hasAnyRole('OPERATIVE', 'ADMIN')")
    public ResponseEntity<List<DocumentShowDto>> getDocumentationForStudent(@PathVariable Long id) {

        List<DocumentShowDto> documentation = documentationService.getDocumentation(id).stream()
                .map(d -> new DocumentShowDto(d))
                .collect(Collectors.toList());

        return ResponseEntity.ok(documentation);
    }

    @PutMapping("/check-document/{idDocument}")
    @PreAuthorize("hasAnyRole('OPERATIVE', 'ADMIN')")
    public ResponseEntity<Void> checkDocument(@RequestBody CheckDocumentDto  checkDocumentDto, @PathVariable Long idDocument) {
        Long idOperative = AuthHelper.getAuthenticatedUserId();

        documentService.checkDocument(checkDocumentDto, idDocument, idOperative);

        return ResponseEntity.ok().build();
    }
}

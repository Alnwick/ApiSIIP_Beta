package com.upiicsa.ApiSIIP_Beta.Service;

import com.upiicsa.ApiSIIP_Beta.Model.Document;
import com.upiicsa.ApiSIIP_Beta.Model.Documentation;
import com.upiicsa.ApiSIIP_Beta.Model.Enum.DocumentType;
import com.upiicsa.ApiSIIP_Beta.Model.Enum.StateDocument;
import com.upiicsa.ApiSIIP_Beta.Model.Student;
import com.upiicsa.ApiSIIP_Beta.Repository.DocumentRepository;
import com.upiicsa.ApiSIIP_Beta.Repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private StudentRepository studentRepository;

    private final Path fileStorageLocation = Paths.get("./uploads").toAbsolutePath().normalize();

    public DocumentService() throws IOException {
        try {
            Files.createDirectories(this.fileStorageLocation);
        }catch (Exception e){
            throw new IOException("No se pudo crear el directorio para almacenar los archivos.", e);
        }
    }

    public Document createInitialDocument(Documentation documentation, DocumentType type) {
        Document document = new Document();
        document.setDocumentation(documentation);
        document.setType(type);
        document.setStateDocument(StateDocument.EN_REVISION);
        return document;
    }

    @Transactional
    public String saveFile(MultipartFile file, String type, Long id) throws IOException {
        String fileName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());

        try {
            Path targetLocation = this.fileStorageLocation.resolve(fileName);

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            String fileUrl = "/uploads/" + fileName;

            saveUrlToDatabase(fileUrl, type, id);

            return fileUrl;

        } catch (IOException ex) {
            throw new IOException("No se pudo guardar el archivo " + fileName + ". Por favor, inténtalo de nuevo!", ex);
        }
    }

    private void saveUrlToDatabase(String fileUrl, String type, Long id) {
        Student student =  studentRepository.getReferenceById(id);
        Documentation documentation = student.getDocumentation();

        System.out.println("Type: " + type);
        Document doc = documentRepository.findByDocumentationAndType(documentation, DocumentType.valueOf(type))
                .orElseThrow(()-> new FileSystemNotFoundException("Document not found"));

        doc.setUrlArchive(fileUrl);
        doc.setStateDocument(StateDocument.EN_REVISION);
        doc.setUploadDate(LocalDateTime.now());

        documentRepository.save(doc);
    }
}

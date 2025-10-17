package com.upiicsa.ApiSIIP_Beta.Service;

import com.upiicsa.ApiSIIP_Beta.Dto.Document.CheckDocumentDto;
import com.upiicsa.ApiSIIP_Beta.Model.Document;
import com.upiicsa.ApiSIIP_Beta.Model.Documentation;
import com.upiicsa.ApiSIIP_Beta.Model.Enum.DocumentType;
import com.upiicsa.ApiSIIP_Beta.Model.Enum.StateDocument;
import com.upiicsa.ApiSIIP_Beta.Model.Student;
import com.upiicsa.ApiSIIP_Beta.Model.UserSIIP;
import com.upiicsa.ApiSIIP_Beta.Repository.DocumentRepository;
import com.upiicsa.ApiSIIP_Beta.Repository.StudentRepository;
import com.upiicsa.ApiSIIP_Beta.Repository.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HistoryService historyService;

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
        Student student =  studentRepository.getReferenceById(id);
        Documentation documentation = student.getDocumentation();

        Document doc = documentRepository.findByDocumentationAndType(documentation, DocumentType.valueOf(type))
                .orElseThrow(()-> new FileSystemNotFoundException("Document not found"));

        switch (doc.getStateDocument()) {
            case EN_REVISION -> {
                String fileUrl = savedFile(file);
                saveUrlToDatabase(fileUrl, doc);
                return fileUrl;
            }

            case INCORRECTO -> {
                String fileUrl = savedFile(file);
                historyService.createHistory(doc);
                saveUrlToDatabase(fileUrl, doc);
                return fileUrl;
            }

            case CORRECTO -> throw new IOException("The state document is not correct. Can't change document.");

            default -> throw new IOException("The state document is not access");
        }
    }

    @Transactional
    public void checkDocument(CheckDocumentDto checkDto, Long idDocument, Long idOperative) {
        Document document = documentRepository.findById(idDocument)
                .orElseThrow(() -> new UsernameNotFoundException("Document not found."));

        if(document.getDocumentation().getUserSIIP() == null){
            UserSIIP userSIIP = userRepository.findById(idOperative)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found."));

            document.getDocumentation().setUserSIIP(userSIIP);
            document.getDocumentation().setAssignmentDate(LocalDateTime.now());
        }

        if(!document.getDocumentation().getUserSIIP().getId().equals(idOperative)){
            throw new RuntimeException("This user is not the same operative as the documentation. UserSIIP: " +
                    document.getDocumentation().getUserSIIP().getId() + " Operative: " + idOperative);
        }

        document.setComment(checkDto.comment());
        document.setStateDocument(StateDocument.valueOf(checkDto.stateDoc()));
        document.setRevisionDate(LocalDateTime.now());

        documentRepository.save(document);
    }

    private String savedFile(MultipartFile file) throws IOException {
        String fileName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());

        try {
            Path targetLocation = this.fileStorageLocation.resolve(fileName);

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/" + fileName;

        } catch (IOException ex) {
            throw new IOException("No se pudo guardar el archivo " + fileName + ". Por favor, inténtalo de nuevo!", ex);
        }
    }

    private void saveUrlToDatabase(String fileUrl, Document doc) {

        doc.setUrlArchive(fileUrl);
        doc.setStateDocument(StateDocument.EN_REVISION);
        doc.setUploadDate(LocalDateTime.now());
        doc.setComment("");
        doc.setRevisionDate(null);

        documentRepository.save(doc);
    }
}

package com.upiicsa.ApiSIIP_Beta.Repository;

import com.upiicsa.ApiSIIP_Beta.Model.Document;
import com.upiicsa.ApiSIIP_Beta.Model.Documentation;
import com.upiicsa.ApiSIIP_Beta.Model.Enum.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    Optional<Document> findByDocumentationAndType(Documentation documentation, DocumentType documentType);
}

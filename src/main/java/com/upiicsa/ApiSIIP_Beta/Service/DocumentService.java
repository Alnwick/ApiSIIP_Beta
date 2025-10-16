package com.upiicsa.ApiSIIP_Beta.Service;

import com.upiicsa.ApiSIIP_Beta.Model.Document;
import com.upiicsa.ApiSIIP_Beta.Model.Documentation;
import com.upiicsa.ApiSIIP_Beta.Model.Enum.DocumentType;
import com.upiicsa.ApiSIIP_Beta.Model.Enum.StateDocument;
import com.upiicsa.ApiSIIP_Beta.Repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    public Document createInitialDocument(Documentation documentation, DocumentType type) {
        Document document = new Document();
        document.setDocumentation(documentation);
        document.setType(type);
        document.setStateDocument(StateDocument.EN_REVISION);
        return document;
    }
}

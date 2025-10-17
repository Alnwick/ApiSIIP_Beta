package com.upiicsa.ApiSIIP_Beta.Service;

import com.upiicsa.ApiSIIP_Beta.Model.Document;
import com.upiicsa.ApiSIIP_Beta.Model.DocumentHistory;
import com.upiicsa.ApiSIIP_Beta.Repository.DocumentHistoRepository;
import com.upiicsa.ApiSIIP_Beta.Repository.DocumentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HistoryService {

    @Autowired
    private DocumentHistoRepository historyRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Transactional
    public void createHistory(Document document) {

        DocumentHistory documentHistory = new DocumentHistory();
        documentHistory.setDocument(document);
        documentHistory.setVersion(documentHistory.getVersion() + 1);
        documentHistory.setUrlArchive(document.getUrlArchive());
        documentHistory.setState(document.getStateDocument());
        documentHistory.setComment(document.getComment());
        documentHistory.setUploadDate(LocalDateTime.now());

        DocumentHistory newHistory = historyRepository.save(documentHistory);

        List<DocumentHistory> list = document.getHistory();
        list.add(newHistory);
        document.setHistory(list);
        documentRepository.save(document);
    }
}

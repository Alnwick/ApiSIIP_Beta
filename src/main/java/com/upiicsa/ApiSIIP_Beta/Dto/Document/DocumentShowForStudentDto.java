package com.upiicsa.ApiSIIP_Beta.Dto.Document;

import com.upiicsa.ApiSIIP_Beta.Model.Document;

import java.time.LocalDateTime;

public record DocumentShowForStudentDto(
        String urlArchive,
        String stateDocument,
        String comment,
        LocalDateTime revisionDate
) {
    public DocumentShowForStudentDto(Document d){
        this(d.getUrlArchive(), d.getStateDocument().toString(), d.getComment(), d.getRevisionDate());
    }
}

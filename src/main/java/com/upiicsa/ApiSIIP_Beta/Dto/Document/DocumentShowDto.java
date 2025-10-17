package com.upiicsa.ApiSIIP_Beta.Dto.Document;

import com.upiicsa.ApiSIIP_Beta.Model.Document;

import java.time.LocalDateTime;

public record DocumentShowDto(
        String urlArchive,
        LocalDateTime uploadDate
) {
    public DocumentShowDto(Document d) {
        this(d.getUrlArchive(), d.getUploadDate());
    }
}

package com.upiicsa.ApiSIIP_Beta.Dto.Document;

import org.springframework.web.multipart.MultipartFile;

public record UploadDocDto(
        String typeDocument,
        MultipartFile file
) {
}

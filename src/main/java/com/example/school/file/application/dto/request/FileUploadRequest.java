package com.example.school.file.application.dto.request;

import java.io.InputStream;

public record FileUploadRequest(
        String originalFilename,
        String contentType,
        long size,
        InputStream inputStream
) {
}

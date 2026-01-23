package com.umc.ourcampus.file.application;

import com.umc.ourcampus.file.application.dto.request.FileUploadRequest;
import com.umc.ourcampus.file.application.dto.response.FileUploadResponse;

public interface FileManager {

    FileUploadResponse uploadFile(FileUploadRequest file, String path);

    String getFileUrl(String path);

    boolean exist(String fileUrl);

    void delete(String fileUrl);
}


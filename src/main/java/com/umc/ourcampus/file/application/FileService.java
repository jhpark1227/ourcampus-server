package com.umc.ourcampus.file.application;

import com.umc.ourcampus.file.application.dto.request.FileUploadRequest;
import com.umc.ourcampus.file.application.dto.response.FileUploadResponse;
import com.umc.ourcampus.file.application.dto.response.ImageUploadResponse;
import com.umc.ourcampus.global.exception.ErrorStatus;
import com.umc.ourcampus.global.exception.ApplicationException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileService {

    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = List.of("jpeg", "png", "jpg", "webp");

    private final FileManager fileManager;

    public ImageUploadResponse uploadReviewImage(FileUploadRequest file) {
        validateImageType(file);
        FileUploadResponse fileUploadResponse = fileManager.uploadFile(file, "review-images");
        return new ImageUploadResponse(fileUploadResponse.url());
    }

    public ImageUploadResponse uploadProfileImage(FileUploadRequest file) {
        validateImageType(file);
        FileUploadResponse fileUploadResponse = fileManager.uploadFile(file, "profile-images");
        return new ImageUploadResponse(fileUploadResponse.url());
    }

    public ImageUploadResponse uploadReturnImage(FileUploadRequest file) {
        validateImageType(file);
        FileUploadResponse fileUploadResponse = fileManager.uploadFile(file, "return-images");
        return new ImageUploadResponse(fileUploadResponse.url());
    }

    private void validateImageType(FileUploadRequest file) {
        String filename = file.originalFilename();
        int dotPosition = filename.lastIndexOf('.');
        String fileExtension = filename.substring(dotPosition + 1);
        if (ALLOWED_IMAGE_EXTENSIONS.contains(fileExtension.toLowerCase())) {
            return;
        }
        throw new ApplicationException(ErrorStatus.FACILITY_NOT_FOUND);
    }
}

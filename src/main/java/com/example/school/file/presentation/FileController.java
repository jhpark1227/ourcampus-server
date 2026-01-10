package com.example.school.file.presentation;

import com.example.school.file.application.FileService;
import com.example.school.file.application.dto.request.FileUploadRequest;
import com.example.school.file.application.dto.response.ImageUploadResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/reviews/images")
    public ImageUploadResponse uploadReviewImage(@RequestParam MultipartFile file) throws IOException {
        FileUploadRequest fileUploadRequest = new FileUploadRequest(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getSize(),
                file.getInputStream()
        );
        return fileService.uploadReviewImage(fileUploadRequest);
    }

    @PostMapping("/members/profile-image")
    public ImageUploadResponse uploadProfileImage(@RequestParam MultipartFile file) throws IOException {
        FileUploadRequest fileUploadRequest = new FileUploadRequest(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getSize(),
                file.getInputStream()
        );
        return fileService.uploadProfileImage(fileUploadRequest);
    }

    @PostMapping("/reservations/images")
    public ImageUploadResponse uploadReturnImage(@RequestParam MultipartFile file) throws IOException {
        FileUploadRequest fileUploadRequest = new FileUploadRequest(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getSize(),
                file.getInputStream()
        );
        return fileService.uploadReturnImage(fileUploadRequest);
    }
}

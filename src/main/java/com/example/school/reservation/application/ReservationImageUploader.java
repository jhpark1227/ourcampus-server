package com.example.school.reservation.application;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ReservationImageUploader {

    String uploadFile(MultipartFile file);

    List<String> uploadFiles(List<MultipartFile> multipartFile);

    void deleteFile(String fileName);
}

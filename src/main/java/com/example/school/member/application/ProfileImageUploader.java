package com.example.school.member.application;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileImageUploader {

    String uploadFile(MultipartFile file);

    List<String> uploadFiles(List<MultipartFile> multipartFile);

    void deleteFile(String fileName);
}

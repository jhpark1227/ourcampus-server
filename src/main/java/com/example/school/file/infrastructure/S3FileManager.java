package com.example.school.file.infrastructure;

import com.example.school.file.application.FileManager;
import com.example.school.file.application.dto.request.FileUploadRequest;
import com.example.school.file.application.dto.response.FileUploadResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3FileManager implements FileManager {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final S3Client s3Client;

    @Override
    public FileUploadResponse uploadFile(FileUploadRequest file, String path) {
        String fileName = createFileName(file.originalFilename());
        String key = path + "/" + fileName;
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(file.contentType())
                .build();
        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.inputStream(), file.size()));
        return new FileUploadResponse(getFileUrl(key));
    }

    public String getFileUrl(String path) {
        return s3Client.utilities()
                .getUrl(builder -> builder
                        .bucket(bucket)
                        .key(path))
                .toString();
    }

    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일(" + fileName + ") 입니다.");
        }
    }

    @Override
    public boolean exist(String imageUrl) {
        HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                .bucket(bucket)
                .key(parseKey(imageUrl))
                .build();
        try {
            s3Client.headObject(headObjectRequest);
        } catch (NoSuchKeyException e) {
            return false;
        }
        return true;
    }

    private String parseKey(String url) {
        String withoutProtocol = url.substring(8);

        if (withoutProtocol.contains(".s3.")) {
            int keyStartIndex = withoutProtocol.indexOf("/") + 1;
            return withoutProtocol.substring(keyStartIndex);
        } else {
            return withoutProtocol.split("/", 3)[2];
        }
    }
}

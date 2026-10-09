package com.esignflow.document_service.storage;

import com.esignflow.document_service.common.exception.DocumentStorageException;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;


@Service
@RequiredArgsConstructor
public class MinioStorageService implements StorageService{
    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    @Override
    public void store(String storageKey, MultipartFile file) {
        try(InputStream stream = file.getInputStream()){
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(storageKey)
                    .stream(stream, file.getSize(), (long) -1)
                    .contentType(file.getContentType())
                    .build()
            );
        } catch (Exception  e) {
            throw new DocumentStorageException("Failed to store document in MinIO", e);
        }
    }
}

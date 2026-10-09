package com.esignflow.document_service.config.storage;


import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MinioBucketInit {
    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;

    @EventListener(ApplicationReadyEvent.class)
    public void initBucket(){
        try{
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if(!exists){
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("Created MinIO bucket: {}", bucketName);
            }
        } catch (Exception e){
            throw new IllegalStateException("Failed to initialize MinIO bucket: " + bucketName);
        }
    }
}

package com.esignflow.document_service.document.dto;

import com.esignflow.document_service.document.domain.Document;

import java.time.Instant;
import java.util.UUID;

public record DocumentResponse(
        UUID id, String title, String description, String status,
        String contentType, Long fileSize, Instant createdAt
) {
    public static DocumentResponse fromDocument(Document document) {
        return new DocumentResponse(
                document.getId(),
                document.getTitle(),
                document.getDescription(),
                document.getStatus().name(),
                document.getContentType(),
                document.getFileSize(),
                document.getCreatedAt()
        );
    }
}

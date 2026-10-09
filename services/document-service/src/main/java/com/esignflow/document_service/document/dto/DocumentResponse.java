package com.esignflow.document_service.document.dto;

import java.time.Instant;
import java.util.UUID;

public record DocumentResponse(
        UUID id, String title, String description, String status,
        String contentType, Long fileSize, Instant createdAt
) {
}

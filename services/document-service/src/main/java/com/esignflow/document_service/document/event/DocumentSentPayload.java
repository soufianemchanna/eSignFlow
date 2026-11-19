package com.esignflow.document_service.document.event;

import java.util.UUID;

public record DocumentSentPayload(
        UUID documentId,
        UUID ownerId,
        String title,
        UUID signerId,
        String signerName,
        String signerEmail,
        int signerOrder
) {
}

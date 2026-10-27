package com.esignflow.document_service.document.event;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record DocumentSentEvent(
        UUID eventId,
        UUID documentId,
        UUID ownerId,
        String title,
        List<SignerRef> signers,
        Instant occurredAt
) {
    public record SignerRef(
            UUID signerId,
            String name,
            String email,
            int order
    ) {
    }
}

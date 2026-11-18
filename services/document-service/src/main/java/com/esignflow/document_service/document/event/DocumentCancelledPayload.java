package com.esignflow.document_service.document.event;

import java.util.UUID;

public record DocumentCancelledPayload(
        UUID documentId,
        UUID ownerId,
        String title
) {
}

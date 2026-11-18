package com.esignflow.document_service.document.event;

import java.time.Instant;
import java.util.UUID;

public record EventEnvelope<T>(
        UUID eventId,
        String eventType,
        Instant occurredAt,
        UUID aggregateId,
        T payload
) {
    public static <T> EventEnvelope<T> of(EventType type, UUID aggregateId, T payload) {
        return new EventEnvelope<>(
                UUID.randomUUID(),
                type.name(),
                Instant.now(),
                aggregateId,
                payload
        );
    }
}

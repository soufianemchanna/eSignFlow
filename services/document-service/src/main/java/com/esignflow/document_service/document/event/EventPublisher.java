package com.esignflow.document_service.document.event;

public interface EventPublisher {
    void publish(EventEnvelope<?> eventEnvelope);
}

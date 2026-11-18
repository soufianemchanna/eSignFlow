package com.esignflow.document_service.document.event;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record DocumentSentEvent(List<DocumentSentPayload> payloads) {}
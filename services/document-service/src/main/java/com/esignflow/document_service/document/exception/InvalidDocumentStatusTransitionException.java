package com.esignflow.document_service.document.exception;

import com.esignflow.document_service.document.domain.DocumentStatus;

public class InvalidDocumentStatusTransitionException extends RuntimeException {
    public InvalidDocumentStatusTransitionException(DocumentStatus from, DocumentStatus to) {
        super("Invalid document status transition from " + from + " to " + to);
    }
}

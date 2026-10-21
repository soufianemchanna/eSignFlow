package com.esignflow.document_service.signer.exception;

public class InvalidDocumentStatusException extends RuntimeException {
    public InvalidDocumentStatusException(String message) {
        super(message);
    }
}

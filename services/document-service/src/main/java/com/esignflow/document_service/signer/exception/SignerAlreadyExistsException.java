package com.esignflow.document_service.signer.exception;

public class SignerAlreadyExistsException extends RuntimeException {
    public SignerAlreadyExistsException(String email) {
        super("Signer with email " + email + " already exists.");
    }
}

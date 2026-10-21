package com.esignflow.document_service.signer.exception;

import java.util.UUID;

public class SignerNotFoundException extends RuntimeException {
    public SignerNotFoundException(UUID signerId) {
        super("Signer with ID " + signerId + " not found.");
    }

}

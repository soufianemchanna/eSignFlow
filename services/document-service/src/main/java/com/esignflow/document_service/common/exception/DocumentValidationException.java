package com.esignflow.document_service.common.exception;

import lombok.Getter;

@Getter
public class DocumentValidationException extends RuntimeException {

    private final String code;

    public DocumentValidationException(String code, String message) {
        super(message);
        this.code = code;
    }
}

package com.esignflow.document_service.common.exception;


import com.esignflow.document_service.document.exception.DocumentNotFoundException;
import com.esignflow.document_service.document.exception.DocumentStorageException;
import com.esignflow.document_service.document.exception.DocumentValidationException;
import com.esignflow.document_service.signer.exception.InvalidDocumentStatusException;
import com.esignflow.document_service.signer.exception.SignerAlreadyExistsException;
import com.esignflow.document_service.signer.exception.SignerNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DocumentValidationException.class)
    public ResponseEntity<ApiError> handleValidation(DocumentValidationException ex, HttpServletRequest req){
        return build(HttpStatus.BAD_REQUEST, ex.getCode(), ex.getMessage(), req);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiError> handleTooLarge(MaxUploadSizeExceededException ex, HttpServletRequest req){
        return build(HttpStatus.CONTENT_TOO_LARGE, "FILE_TOO_LARGE","Uploaded file exceeds the maximum allowed size", req);
    }

    @ExceptionHandler(DocumentStorageException.class)
    public ResponseEntity<ApiError> handleStorage(DocumentStorageException ex, HttpServletRequest req) {
        return build(HttpStatus.SERVICE_UNAVAILABLE, "STORAGE_UNAVAILABLE", "Document storage is temporarily unavailable", req);
    }

    @ExceptionHandler(DocumentNotFoundException.class)
    public ResponseEntity<ApiError> handleDocumentNotFound(DocumentNotFoundException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, "DOCUMENT_NOT_FOUND", ex.getMessage(), req);
    }

    @ExceptionHandler(SignerNotFoundException.class)
    public ResponseEntity<ApiError> handleSignerNotFound(SignerNotFoundException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, "SIGNER_NOT_FOUND", ex.getMessage(), req);
    }

    @ExceptionHandler(SignerAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleSignerAlreadyExists(SignerAlreadyExistsException ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, "SIGNER_ALREADY_EXISTS", ex.getMessage(), req);
    }

    @ExceptionHandler(InvalidDocumentStatusException.class)
    public ResponseEntity<ApiError> handleInvalidDocumentStatus(InvalidDocumentStatusException ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, "INVALID_DOCUMENT_STATUS", ex.getMessage(), req);
    }

    private ResponseEntity<ApiError> build(HttpStatus status, String code, String message, HttpServletRequest req){
        return ResponseEntity.status(status).body(new ApiError(Instant.now(), status.value(), code, message, req.getRequestURI()));
    }
}

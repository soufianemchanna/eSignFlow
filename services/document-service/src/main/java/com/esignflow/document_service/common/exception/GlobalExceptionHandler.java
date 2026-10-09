package com.esignflow.document_service.common.exception;


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

    private ResponseEntity<ApiError> build(HttpStatus status, String code, String message, HttpServletRequest req){
        return ResponseEntity.status(status).body(new ApiError(Instant.now(), status.value(), code, message, req.getRequestURI()));
    }
}

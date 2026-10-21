package com.esignflow.document_service.signer.dto;

import com.esignflow.document_service.signer.domain.Signer;
import com.esignflow.document_service.signer.domain.SignerStatus;

import java.time.Instant;
import java.util.UUID;

public record SignerResponse(
        UUID id,
        String name,
        String email,
        int order,
        SignerStatus status,
        Instant signedAt) {
    public static SignerResponse from(Signer s){
        return new SignerResponse(
                s.getId(),
                s.getName(),
                s.getEmail(),
                s.getSigningOrder(),
                s.getStatus(),
                s.getSignedAt()
        );
    }
}

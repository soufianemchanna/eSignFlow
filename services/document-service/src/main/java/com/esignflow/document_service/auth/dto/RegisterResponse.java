package com.esignflow.document_service.auth.dto;

import java.util.UUID;

public record RegisterResponse(
        UUID userId,
        String email,
        String firstName,
        String lastName
) {
}

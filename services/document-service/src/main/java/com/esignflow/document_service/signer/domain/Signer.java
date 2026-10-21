package com.esignflow.document_service.signer.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "signers")
@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class Signer {
    @Id
    private UUID id;

    @Column(name = "document_id", nullable = false)
    private UUID documentId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(name = "signing_order", nullable = false)
    private int signingOrder;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SignerStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "signed_at")
    private Instant signedAt;

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
    }
}

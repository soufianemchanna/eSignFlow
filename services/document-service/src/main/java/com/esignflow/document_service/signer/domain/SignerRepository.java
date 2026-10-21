package com.esignflow.document_service.signer.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SignerRepository extends JpaRepository<Signer, UUID> {
    List<Signer> findByDocumentIdOrderBySigningOrderAsc(UUID documentId);

    Optional<Signer> findByIdAndDocumentId(UUID id, UUID documentId);

    boolean existsByEmailAndDocumentId(String email, UUID documentId);

    @Query("select coalesce(max(s.signingOrder), 0) from Signer s where s.documentId = :documentId")
    int findMaxOrder(@Param("documentId") UUID documentId);
}

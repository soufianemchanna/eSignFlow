package com.esignflow.document_service.signer.controller;

import com.esignflow.document_service.auth.holder.CurrentUserId;
import com.esignflow.document_service.signer.dto.AddSignerRequest;
import com.esignflow.document_service.signer.dto.SignerResponse;
import com.esignflow.document_service.signer.service.SignerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/documents/{documentId}/signers")
@RequiredArgsConstructor
public class SignerController {
    private final SignerService signerService;

    @PostMapping
    public ResponseEntity<SignerResponse> add(
            @CurrentUserId UUID ownerId,
            @PathVariable UUID documentId,
            @Valid @RequestBody AddSignerRequest request) {
        return ResponseEntity.ok(signerService.add(ownerId, documentId, request));
    }

    @GetMapping
    public List<SignerResponse> list(
            @CurrentUserId UUID ownerId,
            @PathVariable UUID documentId) {
        return signerService.list(ownerId, documentId);
    }

    @DeleteMapping("/{signerId}")
    public ResponseEntity<Void> remove(
            @CurrentUserId UUID ownerId,
            @PathVariable UUID documentId,
            @PathVariable UUID signerId) {
        signerService.remove(ownerId, documentId, signerId);
        return ResponseEntity.noContent().build();
    }
}

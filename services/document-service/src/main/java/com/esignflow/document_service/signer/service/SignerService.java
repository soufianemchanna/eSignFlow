package com.esignflow.document_service.signer.service;


import com.esignflow.document_service.document.domain.Document;
import com.esignflow.document_service.document.domain.DocumentRepository;
import com.esignflow.document_service.document.domain.DocumentStatus;
import com.esignflow.document_service.document.exception.DocumentNotFoundException;
import com.esignflow.document_service.signer.domain.Signer;
import com.esignflow.document_service.signer.domain.SignerRepository;
import com.esignflow.document_service.signer.domain.SignerStatus;
import com.esignflow.document_service.signer.dto.AddSignerRequest;
import com.esignflow.document_service.signer.dto.SignerResponse;
import com.esignflow.document_service.signer.exception.InvalidDocumentStatusException;
import com.esignflow.document_service.signer.exception.SignerAlreadyExistsException;
import com.esignflow.document_service.signer.exception.SignerNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SignerService {
    private final SignerRepository signerRepository;
    private final DocumentRepository documentRepository;

    @Transactional
    public SignerResponse add(UUID ownerId, UUID documentId, AddSignerRequest request){
        Document doc = loadOwned(ownerId, documentId);
        requireDraft(doc);

        String email = request.email().trim().toLowerCase(Locale.ROOT);
        if(signerRepository.existsByEmailAndDocumentId(email, documentId)){
            throw new SignerAlreadyExistsException(email);
        }

        int order = request.order() != null ? request.order() : signerRepository.findMaxOrder(documentId) + 1;
        Signer saved = signerRepository.save(
                Signer.builder()
                        .id(UUID.randomUUID())
                        .email(email)
                        .name(request.name().trim())
                        .signingOrder(order)
                        .documentId(documentId)
                        .status(SignerStatus.PENDING)
                        .build()
        );
        // TODO audit: SIGNER_ADDED
        return SignerResponse.from(saved);
    }

    @Transactional(readOnly=true)
    public List<SignerResponse> list(UUID ownerId, UUID documentID){
        loadOwned(ownerId, documentID);
        return signerRepository.findByDocumentIdOrderBySigningOrderAsc(documentID).stream()
                .map(SignerResponse::from)
                .toList();
    }

    @Transactional
    public void remove(UUID ownerId, UUID documentId, UUID signerId){
        Document doc = loadOwned(ownerId, documentId);
        requireDraft(doc);

        Signer signer = signerRepository.findByIdAndDocumentId(signerId, documentId)
                .orElseThrow(() -> new SignerNotFoundException(signerId));
        signerRepository.delete(signer);
        // TODO audit: SIGNER_REMOVED
    }

    private Document loadOwned(UUID ownerId, UUID documentId){
        Document doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentNotFoundException(documentId));
        if(!doc.getOwnerID().equals(ownerId)){
            throw new DocumentNotFoundException(documentId);
        }
        return doc;
    }

    private void requireDraft(Document doc){
        if(doc.getStatus() != DocumentStatus.DRAFT){
            throw new InvalidDocumentStatusException("Signers can only be modified while document is DRAFT status.");
        }
    }
}

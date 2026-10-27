package com.esignflow.document_service.document.service;


import com.esignflow.document_service.document.domain.Document;
import com.esignflow.document_service.document.domain.DocumentRepository;
import com.esignflow.document_service.document.domain.DocumentStatus;
import com.esignflow.document_service.document.event.DocumentSentEvent;
import com.esignflow.document_service.document.exception.DocumentNotFoundException;
import com.esignflow.document_service.document.exception.InvalidDocumentStatusTransitionException;
import com.esignflow.document_service.document.exception.SendValidationException;
import com.esignflow.document_service.signer.domain.Signer;
import com.esignflow.document_service.signer.domain.SignerRepository;
import com.esignflow.document_service.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class DocumentWorkflowService {
    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private final DocumentRepository documents;
    private final ApplicationEventPublisher events;
    private final StorageService storageService;
    private final SignerRepository signerRepository;

    @Transactional
    public Document send(UUID docId, UUID ownerId){
        Document doc = findOwned(docId, ownerId);

        // state of doc check first
        if(doc.getStatus() != DocumentStatus.DRAFT){
            throw new InvalidDocumentStatusTransitionException(doc.getStatus(), DocumentStatus.PENDING_SIGNATURE);
        }

        // Business validation
        if(!storageService.exists(doc.getStorageKey())){
            throw new SendValidationException("Document file does not exist in storage");
        }
        List<Signer> signers = signerRepository.findByDocumentIdOrderBySigningOrderAsc(docId);
        if(signers.isEmpty()){
            throw new SendValidationException("Document must have at least one signer before sending");
        }
        signers.stream()
                .filter(s -> s.getEmail() == null || !EMAIL.matcher(s.getEmail()).matches())
                .findFirst()
                .ifPresent(s -> {
                    throw new SendValidationException("Signer has an invalid email address : " + s.getEmail());
                });

        // Transition state
        doc.send();
        documents.save(doc);

        // Publish event
        events.publishEvent(new DocumentSentEvent(UUID.randomUUID(), doc.getId(), doc.getOwnerID(), doc.getTitle(),
                signers.stream().map(s -> new DocumentSentEvent.SignerRef(s.getId(), s.getName(), s.getEmail(), s.getSigningOrder())).toList(),
                Instant.now()
                ));

        return doc;

    }

    @Transactional
    public Document cancel(UUID docId, UUID ownerId){
        Document doc = findOwned(docId, ownerId);
        doc.cancel();
        return documents.save(doc);
    }

    private Document findOwned(UUID docId, UUID ownerId){
        return documents.findByIdAndOwnerID(docId, ownerId)
                .orElseThrow(() -> new DocumentNotFoundException(docId));
    }

}

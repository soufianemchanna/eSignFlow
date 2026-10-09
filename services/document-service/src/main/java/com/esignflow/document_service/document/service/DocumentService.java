package com.esignflow.document_service.document.service;


import com.esignflow.document_service.common.exception.DocumentValidationException;
import com.esignflow.document_service.document.domain.Document;
import com.esignflow.document_service.document.domain.DocumentRepository;
import com.esignflow.document_service.document.domain.DocumentStatus;
import com.esignflow.document_service.document.dto.DocumentResponse;
import com.esignflow.document_service.storage.StorageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private static final String ALLOWED_CONTENT_TYPE="application/pdf";

    private final DocumentRepository documentRepository;
    private final StorageService storageService;

    @Value("${esignflow.documents.max-file-size-bytes}")
    private long maxFileSizeBytes;

    @Transactional
    public DocumentResponse createDocument(UUID ownerId, String title, String description, MultipartFile file) {
        validate(file);
        // Implementation for creating document
        UUID documentId = UUID.randomUUID();
        String storageKEy = "documents/" + documentId + "/original.pdf";

        storageService.store(storageKEy, file);

        Document doc = Document.builder()
                .id(documentId)
                .ownerID(ownerId)
                .title(title)
                .description(description)
                .contentType(file.getContentType())
                .fileSize(file.getSize())
                .storageKey(storageKEy)
                .status(DocumentStatus.DRAFT)
                .build();

        return toResponse(documentRepository.save(doc));
    }

    private void validate(MultipartFile file){
        if(file == null || file.isEmpty()){
            throw new DocumentValidationException("VALIDATION_ERROR", "File must not be empty");
        }
        if(!ALLOWED_CONTENT_TYPE.equals(file.getContentType())){
            throw new DocumentValidationException("UNSUPPORTED_CONTENT_TYPE", "Only PDF files are allowed");
        }
        if(file.getSize() > maxFileSizeBytes){
            throw new DocumentValidationException("FILE_TOO_LARGE", "File size exceeds the maximum allowed size");
        }
    }

    private DocumentResponse toResponse(Document doc){
        return new DocumentResponse(doc.getId(), doc.getTitle(), doc.getDescription(),
                doc.getStatus().name(), doc.getContentType(),
                doc.getFileSize(), doc.getCreatedAt()
                );
    }
}

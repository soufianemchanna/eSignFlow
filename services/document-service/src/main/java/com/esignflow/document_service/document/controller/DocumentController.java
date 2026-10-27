package com.esignflow.document_service.document.controller;


import com.esignflow.document_service.auth.holder.CurrentUserId;
import com.esignflow.document_service.document.dto.DocumentResponse;
import com.esignflow.document_service.document.service.DocumentService;
import com.esignflow.document_service.document.service.DocumentWorkflowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;
    private final DocumentWorkflowService documentWorkflowService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public DocumentResponse createDocument(
            @CurrentUserId UUID ownerId,
            @RequestPart("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description
            ){
        return documentService.createDocument(ownerId, title, description, file);
    }

    @PostMapping("/{id}/send")
    public DocumentResponse send(@PathVariable UUID id, @CurrentUserId UUID ownerId){
        return DocumentResponse.fromDocument(documentWorkflowService.send(id, ownerId));
    }

    @PostMapping("/{id}/cancel")
    public DocumentResponse cancel(@PathVariable UUID id, @CurrentUserId UUID ownerId){
        return DocumentResponse.fromDocument(documentWorkflowService.cancel(id, ownerId));
    }


}

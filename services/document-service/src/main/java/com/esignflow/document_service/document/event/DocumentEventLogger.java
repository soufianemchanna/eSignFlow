package com.esignflow.document_service.document.event;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class DocumentEventLogger {

    private static final Logger log = LoggerFactory.getLogger(DocumentEventLogger.class);

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleDocumentEvent(DocumentSentEvent event) {
        log.info("event=DOCUMENT_SENT documentId={} ownerId={} signers={}",
                event.documentId(), event.ownerId(), event.signers().size());
    }

}

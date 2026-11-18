package com.esignflow.document_service.document.event;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class DocumentEventListener {
    private static final Logger log = LoggerFactory.getLogger(DocumentEventListener.class);
    private final EventPublisher publisher;

    public DocumentEventListener(EventPublisher publisher) {
        this.publisher = publisher;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onDocumentSent(DocumentSentEvent event){
        for(DocumentSentPayload p : event.payloads()){
            safePublish(EventEnvelope.of(EventType.DOCUMENT_SENT_FOR_SIGNATURE, p.documentId(), p));
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onDocumentCancelled(DocumentCancelledEvent event){
        var p = event.payload();
        safePublish(EventEnvelope.of(EventType.DOCUMENT_CANCELLED, p.documentId(), p));
    }

    private void safePublish(EventEnvelope<?> eventEnvelope){
        try{
            publisher.publish(eventEnvelope);
        } catch (Exception e){
            log.error("Event lost (outbox needed) eventId={} type={}",
                    eventEnvelope.eventId(), eventEnvelope.eventType(), e);
        }
    }
}

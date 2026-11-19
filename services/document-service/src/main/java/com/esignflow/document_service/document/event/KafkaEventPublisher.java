package com.esignflow.document_service.document.event;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class KafkaEventPublisher implements EventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaEventPublisher.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final String topic;

    public KafkaEventPublisher(KafkaTemplate<String, String> kafkaTemplate,
                               ObjectMapper objectMapper,
                               @Value("${esignflow.kafka.topics.document-events}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.topic = topic;
    }

    @Override
    public void publish(EventEnvelope<?> eventEnvelope) {
        final String json;
        try {
            json = objectMapper.writeValueAsString(eventEnvelope);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot serialize event " + eventEnvelope.eventId(), e);
        }
        kafkaTemplate.send(topic, eventEnvelope.aggregateId().toString(), json)
                .whenComplete((result, ex)->{
                    if(ex!=null){
                        log.error("Failed to publish event {} to Kafka topic {}: {}", eventEnvelope.eventId(), topic, ex.getMessage(), ex);
                    } else {
                        log.info("Published event {} to Kafka topic {} partition {} offset {}", eventEnvelope.eventId(), topic, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
                    }
                });
    }
}

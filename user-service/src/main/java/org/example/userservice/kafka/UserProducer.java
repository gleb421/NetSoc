package org.example.userservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.userservice.event.UserCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserProducer {
    @Autowired
    private final KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;
    public void sendUserCreated(UserCreatedEvent event) {
        kafkaTemplate.send("user-registered", event);
        log.info("✅ Sent user created event to Kafka: {}", event);
    }
}

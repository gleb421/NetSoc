package org.example.userservice.kafka;

import lombok.RequiredArgsConstructor;
import org.example.common.event.UserCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserProducer {
    @Autowired
    private final KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;
    public void sendUserCreated(UserCreatedEvent event) {
        kafkaTemplate.send("user.created", event);
    }
}

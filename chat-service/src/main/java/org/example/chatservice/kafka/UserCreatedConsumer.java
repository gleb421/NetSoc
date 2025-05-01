package org.example.chatservice.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.common.event.UserCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static org.example.common.kafka.KafkaTopics.USER_REGISTERED;

@Slf4j
@Component
public class UserCreatedConsumer {

    @KafkaListener(topics = USER_REGISTERED, groupId = "chat-service", containerFactory = "kafkaListenerContainerFactory")
    public void listen(UserCreatedEvent event, ConsumerRecord<String, UserCreatedEvent> record) {
        log.info("📩 Получено сообщение о новом пользователе: {}", event);
        // Здесь можно делать бизнес-логику, например, создавать профиль чата
    }
}

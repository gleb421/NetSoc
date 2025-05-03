package org.example.chatservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.chatservice.dto.ChatMessageDto;
import org.example.chatservice.event.NewMessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static org.example.chatservice.kafka.KafkaTopics.NEW_MESSAGE;


@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaMessageProducer {
    @Autowired
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(ChatMessageDto message) {
        kafkaTemplate.send(KafkaTopics.CHAT_MESSAGE_TOPIC, message);
        log.info("✅ Sent message to Kafka: {}", message);
    }

    public void sendNewMessageEvent(NewMessageEvent event) {
        kafkaTemplate.send(NEW_MESSAGE, event);
    }
}


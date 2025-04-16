package org.example.chatservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.common.dto.ChatMessageDto;
import org.example.common.kafka.KafkaTopics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaMessageProducer {
    @Autowired
    private final KafkaTemplate<String, ChatMessageDto> kafkaTemplate;

    public void sendMessage(ChatMessageDto message) {
        kafkaTemplate.send(KafkaTopics.CHAT_MESSAGE_TOPIC, message);
        log.info("✅ Sent message to Kafka: {}", message);
    }
}

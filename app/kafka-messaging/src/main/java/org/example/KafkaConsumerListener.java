package org.example;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerListener {

    @KafkaListener(topics = "chat-messages", groupId = "netSoc-group")
    public void listen(String message) {
        System.out.println("📥 Kafka Consumer получил сообщение: " + message);
        // Здесь ты можешь:
        // - сохранить в БД
        // - отправить через WebSocket
        // - записать в Redis
    }
}

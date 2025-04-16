package org.example.notificationservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.common.dto.ChatMessageDto;

import org.example.common.event.UserCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "chat-messages", groupId = "notification-group")
    public void handleChatMessage(String messageJson) {
        try {
            ChatMessageDto message = objectMapper.readValue(messageJson, ChatMessageDto.class);
            System.out.printf("💬 Новое сообщение от %d к %d: %s%n",
                    message.getSenderId(), message.getReceiverId(), message.getContent());
            // здесь можно вызвать: notificationService.sendPush(), sendWebSocket(), etc.
        } catch (Exception e) {
            System.err.println("❌ Ошибка парсинга chat-messages: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "user-registered", groupId = "notification-group")
    public void handleUserRegistration(String messageJson) {
        try {
            UserCreatedEvent user = objectMapper.readValue(messageJson, UserCreatedEvent.class);
            System.out.printf("🎉 Новый пользователь: %s (ID: %d)%n", user.getUsername(), user.getUserId());
            // здесь можно вызвать: emailService.sendWelcomeEmail()
        } catch (Exception e) {
            System.err.println("❌ Ошибка парсинга user-registered: " + e.getMessage());
        }
    }
}

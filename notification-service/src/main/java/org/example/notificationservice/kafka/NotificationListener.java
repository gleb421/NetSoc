package org.example.notificationservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import org.example.notificationservice.dto.ChatMessageDto;
import org.example.notificationservice.event.NewMessageEvent;
import org.example.notificationservice.event.UserCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static org.example.notificationservice.kafka.KafkaTopics.NEW_MESSAGE;

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
        } catch (Exception e) {
            System.err.println("❌ Ошибка парсинга user-registered: " + e.getMessage());
        }
    }

    @KafkaListener(topics = NEW_MESSAGE, groupId = "notification-group")
    public void handleNewMessage(String json) {
        try {
            NewMessageEvent event = objectMapper.readValue(json, NewMessageEvent.class);
            System.out.printf("🔔 Сообщение для %d от %s: %s%n",
                    event.getReceiverId(), event.getSenderUsername(), event.getContent());
        } catch (Exception e) {
            System.err.println("Ошибка парсинга NewMessageEvent: " + e.getMessage());
        }
    }



}

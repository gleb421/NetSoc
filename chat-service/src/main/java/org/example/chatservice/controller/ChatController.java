package org.example.chatservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.chatservice.kafka.KafkaMessageProducer;
import org.example.chatservice.model.ChatMessage;
import org.example.chatservice.service.ChatMessageService;
import org.example.chatservice.service.UserServiceClient;
import org.example.common.dto.ChatMessageDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService messageService;
    private final KafkaMessageProducer kafkaProducer;
    private final UserServiceClient userClient; // 👈 REST-клиент к user-service

    @MessageMapping("/chat")
    public void processMessage(ChatMessageDto message, Principal principal) {
        String senderUsername = principal.getName();

        ChatMessage saved = messageService.saveMessage(
                message.getSenderId(),
                senderUsername,
                message.getReceiverId(),
                message.getContent()
        );

        messagingTemplate.convertAndSendToUser(
                String.valueOf(message.getReceiverId()),
                "/queue/messages",
                saved
        );

        // Создаём DTO и публикуем
        ChatMessageDto dto = new ChatMessageDto(message.getSenderId(),
                message.getReceiverId(), message.getContent(), LocalDateTime.now());
        kafkaProducer.sendMessage(dto);
    }

}

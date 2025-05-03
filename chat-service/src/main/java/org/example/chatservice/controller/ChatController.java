package org.example.chatservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.chatservice.dto.ChatMessageDto;
import org.example.chatservice.event.NewMessageEvent;
import org.example.chatservice.kafka.KafkaMessageProducer;
import org.example.chatservice.model.ChatMessage;
import org.example.chatservice.service.ChatMessageService;
import org.example.chatservice.service.UserServiceClient;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.time.LocalDateTime;

import static org.apache.kafka.common.requests.DeleteAclsResponse.log;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService messageService;
    private final KafkaMessageProducer kafkaProducer;
    private final UserServiceClient userServiceClient;

    @MessageMapping("/chat")
    public void processMessage(ChatMessageDto message, Principal principal) {
        String senderUsername = principal.getName(); // ✅ из сессии WebSocket
        log.info("📨 Отправка от {} (id={})", senderUsername, message.getSenderId());

        ChatMessage saved = messageService.saveMessage(
                message.getSenderId(),  // из фронта
                senderUsername,
                message.getReceiverId(),
                message.getContent()
        );

        messagingTemplate.convertAndSendToUser(
                String.valueOf(message.getReceiverId()),
                "/queue/messages",
                saved
        );

        ChatMessageDto dto = new ChatMessageDto(
                message.getSenderId(),
                message.getReceiverId(),
                message.getContent(),
                LocalDateTime.now()
        );
        kafkaProducer.sendMessage(dto);
        kafkaProducer.sendNewMessageEvent(
                new NewMessageEvent(
                        message.getReceiverId(),
                        senderUsername,
                        message.getContent(),
                        LocalDateTime.now()
                )
        );
    }

    @GetMapping("/chat")
    public String chatPage() {
        return "chat";
    }
}

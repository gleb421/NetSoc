package org.example.controller;

import jakarta.validation.Valid;
import java.security.Principal;
import org.example.UserService;
import org.example.ChatMessageService;
import org.example.domain.ChatMessage;
import org.example.domain.User;
import org.example.dto.MessageRequestDto;
import org.example.dto.MessageResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService messageService;
    private final UserService userService;

    public ChatController(@Autowired SimpMessagingTemplate messagingTemplate,
                          @Autowired ChatMessageService messageService,
                          @Autowired UserService userService) {
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
        this.userService = userService;
    }

    @MessageMapping("/chat")
    public void processMessage(@Payload @Valid MessageRequestDto messageDto, Principal principal) {
        // Определяем отправителя из Principal (надежнее, чем из данных клиента)
        String senderUsername = principal.getName();
        User sender = userService.findByUsername(senderUsername)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        // Находим получателя по ID из DTO
        User recipient = userService.findById(messageDto.getRecipientId())
                .orElseThrow(() -> new RuntimeException("Recipient not found"));

        // Создаем и сохраняем сообщение (ChatMessage – сущность JPA)
        ChatMessage message = new ChatMessage();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setContent(messageDto.getContent());
        message.setTimestamp(java.time.LocalDateTime.now());
        ChatMessage savedMessage = messageService.saveMessage(sender, recipient, message.getContent());

        // Готовим DTO ответа для клиента
        MessageResponseDto response = new MessageResponseDto();
        response.setId(savedMessage.getId());
        response.setSenderId(sender.getId());
        response.setSenderUsername(sender.getUsername());
        response.setContent(savedMessage.getContent());
        response.setTimestamp(savedMessage.getTimestamp());

        // Отправляем сообщение получателю по его user‑очереди (Spring добавит префикс /user автоматически)
        messagingTemplate.convertAndSendToUser(recipient.getUsername(), "/queue/messages", response);
    }
}

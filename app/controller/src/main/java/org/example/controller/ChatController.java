package org.example.controller;

import jakarta.validation.Valid;

import java.security.Principal;

import org.example.KafkaProducerService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService messageService;
    private final UserService userService;
    private KafkaProducerService kafkaProducer;
    public ChatController(@Autowired SimpMessagingTemplate messagingTemplate,
                          @Autowired ChatMessageService messageService,
                          @Autowired UserService userService,
                          @Autowired KafkaProducerService kafkaProducer) {
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
        this.userService = userService;
        this.kafkaProducer = kafkaProducer;
    }
    @GetMapping("/test-kafka")
    @ResponseBody
    public String testKafka() {
        kafkaProducer.sendMessage("Привет от netSoc!");
        return "Сообщение отправлено!";
    }
    @MessageMapping("/chat")
    public void processMessage(ChatMessage message) {
        User sender = userService.findById(message.getSender().getId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User recipient = userService.findById(message.getRecipient().getId())
                .orElseThrow(() -> new RuntimeException("Recipient not found"));

        ChatMessage savedMessage = messageService.saveMessage(sender, recipient, message.getContent());

        // Отправить получателю
        messagingTemplate.convertAndSendToUser(
                String.valueOf(recipient.getId()),
                "/queue/messages",
                savedMessage
        );
        kafkaProducer.sendMessage(message.getSender().getUsername() + ": " + message.getContent());
        System.out.println("Отправка WebSocket сообщения sender=" + sender.getUsername() + " -> recipient=" + recipient.getUsername());

    }
}

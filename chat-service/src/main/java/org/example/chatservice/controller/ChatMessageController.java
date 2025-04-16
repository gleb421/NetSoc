package org.example.chatservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.chatservice.dto.MessageRequestDto;
import org.example.chatservice.model.ChatMessage;
import org.example.chatservice.service.ChatMessageService;
import org.example.chatservice.service.UserServiceClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService messageService;
    private final UserServiceClient userClient;

    @PostMapping
    public ChatMessage sendMessage(@RequestBody MessageRequestDto dto) {
        Long senderId = userClient.getCurrentUserId(); // или получи из токена
        String senderUsername = userClient.getUsernameById(senderId);

        return messageService.saveMessage(senderId, senderUsername, dto.getRecipientId(), dto.getContent());
    }

    @GetMapping("/{friendId}")
    public List<ChatMessage> getMessages(@PathVariable Long friendId) {
        Long currentUserId = userClient.getCurrentUserId(); // из токена/session
        return messageService.getMessagesBetweenUsers(currentUserId, friendId);
    }
}

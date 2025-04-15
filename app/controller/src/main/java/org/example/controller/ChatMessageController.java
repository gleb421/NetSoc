package org.example.controller;

import lombok.Getter;
import lombok.Setter;
import org.example.domain.ChatMessage;
import org.example.ChatMessageService;
import org.example.UserService;
import org.example.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class ChatMessageController {
    private final UserService userService;

    private final ChatMessageService messageService;

    public ChatMessageController(@Autowired UserService userService, @Autowired ChatMessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    @GetMapping("/{friendId}")
    public List<ChatMessage> sages(@PathVariable String friendId, Principal principal) {
        if (principal == null) {
            throw new RuntimeException("Principal is null");
        }
        User currentUser = userService.findByUsername(principal.getName()).orElseThrow(
                () -> new RuntimeException("User not found"));
        return messageService.getMessagesBetweenUsers(currentUser.getId(), Long.parseLong(friendId));
    }

    @PostMapping
    public ChatMessage sendMessage(@RequestBody SendMessageRequest request, Principal principal) {
        if (principal == null) {
            throw new RuntimeException("Principal is null");
        }
        User sender = userService.findByUsername(principal.getName()).orElseThrow(
                () -> new RuntimeException("Sender not found"));
        User recipient = userService.findById(request.getRecipientId())
                .orElseThrow(() -> new RuntimeException("Recipient not found"));

        return messageService.saveMessage(sender, recipient, request.getContent());
    }

    @Getter
    @Setter
    private static class SendMessageRequest {
        private Long recipientId;
        private String content;
    }
}
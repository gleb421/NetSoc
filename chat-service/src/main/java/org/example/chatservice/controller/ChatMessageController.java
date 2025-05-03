package org.example.chatservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.chatservice.dto.MessageRequestDto;
import org.example.chatservice.dto.UserDto;
import org.example.chatservice.model.ChatMessage;
import org.example.chatservice.service.ChatMessageService;
import org.example.chatservice.service.UserServiceClient;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService messageService;
    private final UserServiceClient userClient;
    private final UserServiceClient userServiceClient;

    @PostMapping("/messages")
    public ChatMessage sendMessage(@RequestBody MessageRequestDto dto) {
        UserDto currentUser = userClient.getCurrentUser();
        return messageService.saveMessage(
                currentUser.getId(),
                currentUser.getUsername(),
                dto.getRecipientId(),
                dto.getContent()
        );
    }
    @GetMapping("/current-user")
    public UserDto getCurrentUser() {
        return userClient.getCurrentUser();
    }


    @GetMapping("/messages/{friendId}")
    public List<ChatMessage> getMessages(@PathVariable Long friendId) {
        UserDto currentUser = userClient.getCurrentUser();
        return messageService.getMessagesBetweenUsers(currentUser.getId(), friendId);
    }
    @GetMapping("/friends")
    public Set<UserDto> getFriends() {
        System.out.println("🔥 ChatService: /api/friends вызван");
        UserDto currentUser = userClient.getCurrentUser();
        UserDto[] friends = userServiceClient.getFriends(currentUser.getId());
        return new HashSet<>(Arrays.asList(friends));
    }
}
package org.example;

import org.example.domain.ChatMessage;
import org.example.domain.User;
import org.example.repositories.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserService userService;

    public ChatMessageService(@Autowired ChatMessageRepository chatMessageRepository,
                              @Autowired UserService userService) {
        this.chatMessageRepository = chatMessageRepository;
        this.userService = userService;
    }

    public ChatMessage saveMessage(User sender, User recipient, String content) {
        ChatMessage message = new ChatMessage();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setContent(content);
        message.setTimestamp(java.time.LocalDateTime.now());
        return chatMessageRepository.save(message);
    }

    public List<ChatMessage> getMessagesBetweenUsers(Long userId1, Long userId2) {
        return chatMessageRepository.findConversation(userId1, userId2);
    }
}

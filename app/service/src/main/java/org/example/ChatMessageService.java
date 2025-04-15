package org.example;

import org.example.domain.ChatMessage;
import org.example.domain.User;
import org.example.repositories.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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

//    @Cacheable(value = "chatHistory", key = "T(java.util.Arrays).asList(#id1, #id2).toString()")
    public List<ChatMessage> getMessagesBetweenUsers(Long id1, Long id2) {
        return chatMessageRepository.findConversation(id1, id2);
    }
}

package org.example.chatservice.service;


import lombok.RequiredArgsConstructor;
import org.example.chatservice.model.ChatMessage;
import org.example.chatservice.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository messageRepo;

    public ChatMessage saveMessage(Long senderId, String senderUsername, Long recipientId, String content) {
        ChatMessage msg = new ChatMessage();
        msg.setSenderId(senderId);
        msg.setSenderUsername(senderUsername);
        msg.setRecipientId(recipientId);
        msg.setContent(content);
        msg.setTimestamp(LocalDateTime.now());
        return messageRepo.save(msg);
    }

    public List<ChatMessage> getMessagesBetweenUsers(Long userId1, Long userId2) {
        return messageRepo.findConversation(userId1, userId2);
    }
}

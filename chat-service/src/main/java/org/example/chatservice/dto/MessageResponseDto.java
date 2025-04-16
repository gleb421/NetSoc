package org.example.chatservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessageResponseDto {
    private Long id;
    private Long senderId;
    private String senderUsername;
    private String content;
    private LocalDateTime timestamp;
}

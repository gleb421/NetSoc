package org.example.chatservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class MessageResponseDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long senderId;
    private String senderUsername;
    private String content;
    private LocalDateTime timestamp;
}
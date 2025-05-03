package org.example.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long senderId;
    private Long receiverId;
    private String content;
    private LocalDateTime timestamp;
}
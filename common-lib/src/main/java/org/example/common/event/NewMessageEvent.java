package org.example.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewMessageEvent {
    private Long receiverId;
    private String senderUsername;
    private String content;
    private LocalDateTime timestamp;
}
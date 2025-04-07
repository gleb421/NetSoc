package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequestDto {
    @NotNull(message = "Recipient id must not be null")
    private Long recipientId;

    @NotBlank(message = "Content must not be blank")
    private String content;
}

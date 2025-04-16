package org.example.chatservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@Setter
public class MessageRequestDto {
    @NotNull
    private Long recipientId;

    @NotBlank
    private String content;
}

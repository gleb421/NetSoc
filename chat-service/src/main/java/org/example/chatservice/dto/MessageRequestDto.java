package org.example.chatservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.io.Serializable;


@Getter
@Setter
public class MessageRequestDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long recipientId;

    @NotBlank
    private String content;
}
//package org.example.controller;
//
//import org.example.domain.ChatMessage;
//import org.example.domain.User;
//import org.example.ChatMessageService;
//import org.example.UserService;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//public class WebSocketChatControllerTest {
//
//    private final SimpMessagingTemplate messagingTemplate = Mockito.mock(SimpMessagingTemplate.class);
//    private final ChatMessageService chatMessageService = Mockito.mock(ChatMessageService.class);
//    private final UserService userService = Mockito.mock(UserService.class);
//    private final WebSocketChatController controller = new WebSocketChatController(messagingTemplate, chatMessageService, userService);
//
//    @Test
//    public void testProcessMessage() {
//        User sender = new User();
//        sender.setId(1L);
//        User recipient = new User();
//        recipient.setId(2L);
//        ChatMessage message = new ChatMessage();
//        message.setSender(sender);
//        message.setRecipient(recipient);
//        message.setContent("Hello");
//
//        when(userService.findById(1L)).thenReturn(Optional.of(sender));
//        when(userService.findById(2L)).thenReturn(Optional.of(recipient));
//        when(chatMessageService.saveMessage(any(), any(), any())).thenReturn(message);
//
//        controller.processMessage(message);
//
//        verify(messagingTemplate).convertAndSendToUser("2", "/queue/messages", message);
//    }
//}
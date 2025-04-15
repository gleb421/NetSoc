//package org.example.controller;
//
//import org.example.domain.ChatMessage;
//import org.example.domain.User;
//import org.example.ChatMessageService;
//import org.example.UserService;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.Collections;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//public class ChatMessageControllerTest {
//
//    private final UserService userService = Mockito.mock(UserService.class);
//    private final ChatMessageService messageService = Mockito.mock(ChatMessageService.class);
//    private final ChatMessageController controller = new ChatMessageController(userService, messageService);
//    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
//
//
//    @Test
//    @WithMockUser(username = "user")
//    public void testsages() throws Exception {
//        User user = new User();
//        user.setId(1L);
//        when(userService.findByUsername(any())).thenReturn(Optional.of(user));
//        when(messageService.sgetMessagesBetweenUsers(anyLong(), anyLong())).thenReturn(Collections.emptyList());
//
//        mockMvc.perform(get("/api/messages/2"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockUser(username = "user")
//    public void testSendMessage() throws Exception {
//        mockMvc.perform(post("/api/messages")
//                        .principal(() -> "user") // <-- подставляем mock Principal
//                        .contentType("application/json")
//                        .content("{\"recipientId\": 2, \"content\": \"Hello\"}"))
//                .andExpect(status().isOk());
//        User user = new User();
//        user.setId(1L);
//        when(userService.findByUsername(any())).thenReturn(Optional.of(user));
//        when(userService.findById(anyLong())).thenReturn(Optional.of(new User()));
//        when(messageService.saveMessage(any(), any(), any())).thenReturn(new ChatMessage());
//
//        mockMvc.perform(post("/api/messages")
//                        .contentType("application/json")
//                        .content("{\"recipientId\": 2, \"content\": \"Hello\"}"))
//                .andExpect(status().isOk());
//    }
//}
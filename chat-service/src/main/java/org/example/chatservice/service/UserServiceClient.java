package org.example.chatservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class UserServiceClient {
    private final RestTemplate restTemplate;

    public String getUsernameById(Long userId) {
        return restTemplate.getForObject("http://user-service/api/users/" + userId + "/username", String.class);
    }

    public Long getCurrentUserId() {
        // можно временно возвращать 1L, если нет авторизации
        return 1L;
    }
}

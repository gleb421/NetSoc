package org.example.chatservice.service;

import lombok.RequiredArgsConstructor;
import org.example.common.dto.UserDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import org.springframework.beans.factory.ObjectFactory;
@Component
@RequiredArgsConstructor
public class UserServiceClient {

    private final RestTemplate restTemplate;

    public UserDto getCurrentUser() {
        try {
            return restTemplate.getForObject("http://localhost:8081/api/current-user", UserDto.class);
        } catch (Exception e) {
            e.printStackTrace(); // 🔁 пока лог для отладки
            return null;
        }
    }

    public UserDto[] getFriends(Long userId) {
        return restTemplate.getForObject("http://localhost:8081/api/users/" + userId + "/friends", UserDto[].class);
    }
}
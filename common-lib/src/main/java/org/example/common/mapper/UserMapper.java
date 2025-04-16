package org.example.common.mapper;

import org.example.common.dto.UserDto;
import org.example.common.event.UserCreatedEvent;

public class UserMapper {
    public static UserCreatedEvent toEvent(UserDto user) {
        return new UserCreatedEvent(user.getId(), user.getUsername());
    }

    public static UserDto fromEvent(UserCreatedEvent event) {
        return new UserDto(event.getUserId(), event.getUsername());
    }
}

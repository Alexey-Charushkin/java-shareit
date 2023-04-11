package ru.practicum.shareit.user.dto;

import lombok.Data;
import ru.practicum.shareit.user.User;

@Data
public class UserDto {

    private Long id;

    private String name;

    private String email;

    public UserDto(User user) {
        this.id = user.getUserId();
        this.name = user.getName();
        this.email = user.getEmail();
    }
}

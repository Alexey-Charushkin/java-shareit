package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(User user);

    UserDto update(Long userid, User user);

    UserDto getById(Long userId);

    List<UserDto> getAll();

    UserDto deleteById(Long userId);
}

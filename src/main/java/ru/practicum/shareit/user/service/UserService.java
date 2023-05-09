package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(UserDto userDto);

    UserDto update(UserDto userDto);

    UserDto getById(Long userId);


    List<UserDto> getAll();

    void deleteById(Long userId);
}

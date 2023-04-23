package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserDao {

    UserDto add(User user);

    UserDto update(User user);

    List<UserDto> getAll();

    UserDto remove(Long id);

    UserDto get(Long id);

}

package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserDao {

    UserDto add(User user);

    public UserDto update(UserDto user);

    List<User> getAll();

    void remove(Long id);

    User get(Long id);

}

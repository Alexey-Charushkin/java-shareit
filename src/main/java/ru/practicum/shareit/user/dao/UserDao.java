package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.User;

import java.util.HashMap;
import java.util.Map;

public interface UserDao {

    Map<Long, User> users = new HashMap<>();

    void add(Long id, User user);

    Map<Long, User> getAll();

    User remove(Long id);

    User get(Long id);

    boolean containsKey(Long id);

}

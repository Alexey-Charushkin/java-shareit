package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.Map;

@Service
public class InMemoryUserDao implements UserDao {

    @Override
    public void add(Long id, User user) {
        users.put(user.getUserId(), user);
    }

    @Override
    public Map<Long, User> getAll() {
        return users;
    }

    @Override
    public User remove(Long id) {
        return users.remove(id);
    }

    @Override
    public User get(Long id) {
        return users.get(id);
    }

//    @Override
//    public void addItem(Long id, Item item) {
//        users.put(id);
//    }

    @Override
    public boolean containsKey(Long id) {
        return users.containsKey(id);
    }

}

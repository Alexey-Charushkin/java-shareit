package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.HashMap;
import java.util.Map;

public interface ItemDao {

    Map<Long, Item> items = new HashMap<>();

    void add(Long id, Item item);

    Map<Long, Item> getAll();

    Item remove(Long id);

    Item get(Long id);

    boolean containsKey(Long id);
}

package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ItemDao {

    void add(Long id, Item item);

    Map<Long, Item> getAll();

    Item remove(Long id);

    Item get(Long id);

    List<Item> search(String query);

    boolean containsKey(Long id);
}

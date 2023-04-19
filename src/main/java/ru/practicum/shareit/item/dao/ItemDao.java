package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ItemDao {

    void add(Item item);

    ItemDto update(Long userId, ItemDto itemDto);

    //    Map<Long, Item> getAll();
//
//    Item remove(Long id);
//
    Item get(Long id);
//
//    List<Item> search(String query);
//
//    boolean containsKey(Long id);
}

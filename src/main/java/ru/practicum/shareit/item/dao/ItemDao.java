package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Stream;

public interface ItemDao {

    void add(Item item);

    ItemDto update(Long userId, ItemDto itemDto);

    List<Item> getAllItemsByUser(Long userId);

    Item remove(Long id);

    Item get(Long id);

    Stream<Item> search(String query);

}

package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto create(Long userId, Item item);

    ItemDto update(Long itemId, Item item);

    ItemDto getById(Long itemId);

    List<ItemDto> getAll();

    ItemDto deleteById(Long itemId);
}

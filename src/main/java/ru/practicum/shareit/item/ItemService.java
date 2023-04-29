package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto create(Long userId, ItemDto itemDto);

    ItemDto update(Long itemId, ItemDto itemDto);

    ItemDto getItemById(Long itemId);

    List<ItemDto> getAllItemsByUserId(Long userId);
//
//    ItemDto deleteById(Long itemId);
//
//    List<ItemDto> searchItems(String query);

}

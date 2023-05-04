package ru.practicum.shareit.item;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto create(Long userId, ItemDto itemDto);

    ItemDto update(Long itemId, ItemDto itemDto);

    ItemDto getItemById(Long itemId, Long id);

    List<ItemDto> getAllItemsByUserId(Long userId);

    void deleteById(Long itemId);

    List<ItemDto> searchItems(String query);

}

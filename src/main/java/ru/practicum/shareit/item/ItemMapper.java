package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    public ItemDto itemToItemDto(Item item) {
       return new ItemDto(item, getCountRent(item));
    }

    private Integer getCountRent(Item item) {
        return null;
    }
}

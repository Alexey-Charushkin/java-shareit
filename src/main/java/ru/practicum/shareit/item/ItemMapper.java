package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemMapper {

    public ItemDto itemToItemDto(Item item) {
        return new ItemDto(item, getCountRent(item));
    }

    public List<ItemDto> getItemDtoList(List<Item> items) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : items) {
            itemDtoList.add(itemToItemDto(item));
        }
        return itemDtoList;
    }

    private Integer getCountRent(Item item) {
        return null;
    }
}

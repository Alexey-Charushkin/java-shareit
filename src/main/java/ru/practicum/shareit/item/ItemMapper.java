package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }

    public static Item toItem(User user, ItemDto itemDto) {
        return new Item(
                user,
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable()
        );

    }
}

//    public ItemDto itemToItemDto(Item item) {
//        return new ItemDto(item, getCountRent(item));
//    }

//    public List<ItemDto> getItemDtoList(List<Item> items) {
//        List<ItemDto> itemDtoList = new ArrayList<>();
//        for (Item item : items) {
//            itemDtoList.add(itemToItemDto(item));
//        }
//        return itemDtoList;
//    }
//
//    private Integer getCountRent(Item item) {
//        return null;
//    }


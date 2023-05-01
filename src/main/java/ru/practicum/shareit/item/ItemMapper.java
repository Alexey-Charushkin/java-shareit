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
                item.getAvailable().get(),
                item.getRequest()
        );
    }

    public static Item toItem(User user, ItemDto itemDto) {
//        if (itemDto.getAvailable().isEmpty()) {
//            return new Item(
//                    user,
//                    itemDto.getId(),
//                    itemDto.getName(),
//                    itemDto.getDescription(),
//                    null
//            );
//        } else {
            return new Item(
                    user,
                    itemDto.getId(),
                    itemDto.getName(),
                    itemDto.getDescription(),
                    itemDto.getAvailable()
            );
   //     }
    }
}



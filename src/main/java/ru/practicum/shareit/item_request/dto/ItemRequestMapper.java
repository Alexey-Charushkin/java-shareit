package ru.practicum.shareit.item_request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item_request.model.ItemRequest;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ItemRequestMapper {

    public static ItemRequest toItemRequestToSave(ItemRequestDtoToSave itemRequestDtoToSave) {
        return new ItemRequest(
                itemRequestDtoToSave.getDescription()
        );
    }

    public static ItemRequestDtoToReturn toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDtoToReturn(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                itemsToItemDtos(itemRequest.getItems())
        );
    }

    private List<ItemDto> itemsToItemDtos(List<Item> itemList) {
        if (itemList == null) return Collections.emptyList();
        return itemList.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());

    }
}

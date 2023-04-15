package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

public class RequestMapper {
    public ItemRequestDto itemRequestToItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(itemRequest);
    }

}

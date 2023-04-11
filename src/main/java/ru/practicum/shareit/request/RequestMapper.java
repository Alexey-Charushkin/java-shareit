package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

public class RequestMapper {

    public ItemRequestDto ItemRequestToItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(itemRequest);
    }

}

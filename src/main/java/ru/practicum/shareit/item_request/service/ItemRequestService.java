package ru.practicum.shareit.item_request.service;

import ru.practicum.shareit.item_request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto create(Long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestDto> findAllByUserId(Long userId);

    ItemRequestDto findById(Long userId, Long requestId);
}

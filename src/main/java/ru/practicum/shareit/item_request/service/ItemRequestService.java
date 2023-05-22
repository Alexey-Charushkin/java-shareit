package ru.practicum.shareit.item_request.service;

import ru.practicum.shareit.item_request.dto.ItemRequestDtoToReturn;
import ru.practicum.shareit.item_request.dto.ItemRequestDtoToSave;
import ru.practicum.shareit.item_request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDtoToReturn create(Long userId, ItemRequestDtoToSave itemRequestDtoToSave);

    List<ItemRequestDtoToReturn> findAllByUserId(Long userId);

    ItemRequest findById(Long userId, Long requestId);

    List<ItemRequestDtoToReturn> findAllByUserIdToPageable(Long userId, Integer from, Integer size);
}

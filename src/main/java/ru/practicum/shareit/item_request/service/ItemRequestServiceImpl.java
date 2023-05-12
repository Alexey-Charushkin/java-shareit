package ru.practicum.shareit.item_request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item_request.dao.ItemRequestRepository;
import ru.practicum.shareit.item_request.dto.ItemRequestDto;
import ru.practicum.shareit.item_request.dto.ItemRequestMapper;
import ru.practicum.shareit.item_request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ItemRequestServiceImpl implements ItemRequestService {

    private final UserRepository userRepository;

    private final ItemRequestRepository itemRequestRepository;

    private final ItemRepository itemRepository;


    @Override
    public ItemRequestDto create(Long userId, ItemRequestDto itemRequestDto) {

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

        if (itemRequestDto.getDescription() == null || itemRequestDto.getDescription().isEmpty())
            throw new BadRequestException("Description is empty");

        itemRequestDto.setRequestor(user);

        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        itemRequestRepository.save(itemRequest);

        return ItemRequestMapper.toItemRequestDto(itemRequest);
    }

    @Override
    public List<ItemRequestDto> findAllByUserId(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        List<ItemRequest> itemRequests = itemRequestRepository.findAllById(userId, Sort.by(Sort.Direction.DESC,
                "id"));
        return itemRequests.stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .peek(itemRequest -> itemRequest.setItems(findAllByRequestId(itemRequest.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto findById(Long userId, Long requestId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("ItemRequest not found"));

        List<ItemDto> itemDtos = findAllByRequestId(itemRequest.getId());
        itemRequest.setItems(itemDtos);

        return ItemRequestMapper.toItemRequestDto(itemRequest);
    }

    @Override
    public List<ItemRequestDto> findAllByUserIdToPageable(Long userId, Integer from, Integer size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (from == null || size == null) return Collections.emptyList();
        Sort sort = Sort.by(Sort.Direction.DESC, "created");
        Pageable page = PageRequest.of(from, size, sort);

        return itemRequestRepository.findAllByRequestorIdNot(userId, page).stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .peek(itemRequest -> itemRequest.setItems(findAllByRequestId(itemRequest.getId())))
                .collect(Collectors.toList());
    }

    private List<ItemDto> findAllByRequestId(Long itemRequestId) {
        return itemRepository.findAllByRequestId(itemRequestId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

}

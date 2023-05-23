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
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item_request.dao.ItemRequestRepository;
import ru.practicum.shareit.item_request.dto.ItemRequestDtoToReturn;
import ru.practicum.shareit.item_request.dto.ItemRequestDtoToSave;
import ru.practicum.shareit.item_request.dto.ItemRequestMapper;
import ru.practicum.shareit.item_request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Log4j2
public class ItemRequestServiceImpl implements ItemRequestService {

    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;


    @Override
    public ItemRequestDtoToReturn create(Long userId, ItemRequestDtoToSave itemRequestDtoToSave) {

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

        if (itemRequestDtoToSave.getDescription().isEmpty())
            throw new BadRequestException("Description is empty");

        ItemRequest itemRequest = ItemRequestMapper.toItemRequestToSave(itemRequestDtoToSave);
        itemRequest.setRequestor(user);
        itemRequestRepository.save(itemRequest);

        return ItemRequestMapper.toItemRequestDto(itemRequest);
    }

    @Override
    public List<ItemRequestDtoToReturn> findAllByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        List<ItemRequestDtoToReturn> itemRequestDtoToReturns;

        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequestorId(userId, Sort.by(Sort.Direction.DESC,
                "id"));

        List<Long> requestIds = itemRequests.stream()
                .map(ItemRequest::getId)
                .collect(toList());

        Map<Long, List<Item>> itemsByRequestId = itemRepository.findByRequestIdIn(requestIds)
                .stream()
                .collect(Collectors.groupingBy(item -> item.getRequest().getId()));

        if (itemsByRequestId.size() != 0) {
            itemRequestDtoToReturns = itemRequests.stream()
                    .peek(itemRequest -> itemRequest.setItems(itemsByRequestId.getOrDefault(itemRequest.getId(),
                            Collections.emptyList())))
                    .map(ItemRequestMapper::toItemRequestDto)
                    .collect(toList());

        } else {
            itemRequestDtoToReturns = itemRequests.stream()
                    .map(ItemRequestMapper::toItemRequestDto)
                    .collect(Collectors.toList());
        }
        return itemRequestDtoToReturns;
    }

    @Override
    public ItemRequest findById(Long userId, Long requestId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("ItemRequest not found"));

        List<Item> items = findAllByRequestId(itemRequest.getId());
        itemRequest.setItems(items);

        return itemRequest;
    }

    @Override
    public List<ItemRequestDtoToReturn> findAllByUserIdToPageable(Long userId, Integer from, Integer size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (size == null) return Collections.emptyList();
        Sort sort = Sort.by(Sort.Direction.DESC, "created");
        Pageable page = PageRequest.of(from, size, sort);

        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequestorIdNot(userId, page);

        List<Long> requestIds = itemRequests.stream()
                .map(ItemRequest::getId)
                .collect(toList());

        Map<Long, List<Item>> itemsByRequestId = itemRepository.findByRequestIdIn(requestIds)
                .stream()
                .collect(Collectors.groupingBy(item -> item.getRequest().getId()));

        return itemRequests.stream()
                .peek(itemRequest -> itemRequest.setItems(itemsByRequestId.getOrDefault(itemRequest.getId(),
                        Collections.emptyList())))
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(toList());
    }

    private List<Item> findAllByRequestId(Long itemRequestId) {
        return new ArrayList<>(itemRepository.findAllByRequestId(itemRequestId));
    }
}

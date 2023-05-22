package ru.practicum.shareit.item_request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item_request.dto.ItemRequestDto;
import ru.practicum.shareit.item_request.dto.ItemRequestMapper;
import ru.practicum.shareit.item_request.model.ItemRequest;
import ru.practicum.shareit.item_request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {

    @Mock
    private ItemRequestService itemRequestService;

    @InjectMocks
    private ItemRequestController itemRequestController;
    User requestor = new User(1L, "requestorName", "requestorEmail@mail.com");
    ItemRequest request = new ItemRequest(0L, "requestDescription", requestor, null, null);

    ItemRequestDto requestDto = ItemRequestMapper.toItemRequestDto(request);

    @Test
    void create() {
        when(itemRequestService.create(1L, requestDto)).thenReturn(requestDto);

        ItemRequestDto actualItemRequestDto = itemRequestController.create(1L, requestDto);

        assertEquals(requestDto.getId(), actualItemRequestDto.getId());
        assertEquals(requestDto.getDescription(), actualItemRequestDto.getDescription());
        assertEquals(requestDto.getItems(), actualItemRequestDto.getItems());
    }

    @Test
    void findAllByUserId() {
        List<ItemRequestDto> itemRequestDtoList = List.of(new ItemRequestDto(), new ItemRequestDto());
        when(itemRequestService.findAllByUserId(1L)).thenReturn(itemRequestDtoList);

        List<ItemRequestDto> actualItemRequestDtoList = itemRequestController.findAllByUserId(1L);

        assertEquals(itemRequestDtoList.size(), actualItemRequestDtoList.size());
    }

    @Test
    void findById() {
        when(itemRequestService.findById(1L, 1L)).thenReturn(requestDto);

        ItemRequestDto actualItemRequestDto = itemRequestController.findById(1L, 1L);

        assertEquals(requestDto.getId(), actualItemRequestDto.getId());
        assertEquals(requestDto.getDescription(), actualItemRequestDto.getDescription());
        assertEquals(requestDto.getItems(), actualItemRequestDto.getItems());
    }

    @Test
    void findAllById() {
        int from = 0;
        int size = 1;
        List<ItemRequestDto> itemRequestDtoList = List.of(new ItemRequestDto(), new ItemRequestDto());
        when(itemRequestService.findAllByUserIdToPageable(1L, from, size)).thenReturn(itemRequestDtoList);

        List<ItemRequestDto> actualItemRequestDtoList = itemRequestController.findAllById(1L, from, size);

        assertEquals(itemRequestDtoList.size(), actualItemRequestDtoList.size());
    }
}
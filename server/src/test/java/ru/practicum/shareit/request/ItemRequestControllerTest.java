package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.request.dto.ItemRequestDtoToReturn;
import ru.practicum.shareit.request.dto.ItemRequestDtoToSave;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
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

    ItemRequestDtoToReturn requestDto = ItemRequestMapper.toItemRequestDto(request);

    ItemRequestDtoToSave requestDtoToSave = new ItemRequestDtoToSave("save Description");

    @Test
    void create() {
        when(itemRequestService.create(1L, requestDtoToSave)).thenReturn(requestDto);

        ItemRequestDtoToReturn actualItemRequestDtoToReturn = itemRequestController.create(1L, requestDtoToSave);

        assertEquals(requestDto.getId(), actualItemRequestDtoToReturn.getId());
        assertEquals(requestDto.getDescription(), actualItemRequestDtoToReturn.getDescription());
        assertEquals(requestDto.getItems(), actualItemRequestDtoToReturn.getItems());
    }

    @Test
    void findAllByUserId() {
        List<ItemRequestDtoToReturn> itemRequestDtoToReturnList = List.of(new ItemRequestDtoToReturn(), new ItemRequestDtoToReturn());
        when(itemRequestService.findAllByUserId(1L)).thenReturn(itemRequestDtoToReturnList);

        List<ItemRequestDtoToReturn> actualItemRequestDtoToReturnList = itemRequestController.findAllByUserId(1L);

        assertEquals(itemRequestDtoToReturnList.size(), actualItemRequestDtoToReturnList.size());
    }

    @Test
    void findById() {
        when(itemRequestService.findById(1L, 1L)).thenReturn(request);

        ItemRequestDtoToReturn actualItemRequestDtoToReturn = itemRequestController.findById(1L, 1L);

        assertEquals(request.getId(), actualItemRequestDtoToReturn.getId());
        assertEquals(request.getDescription(), actualItemRequestDtoToReturn.getDescription());
    }

    @Test
    void findAllById() {
        int from = 0;
        int size = 1;
        List<ItemRequestDtoToReturn> itemRequestDtoToReturnList = List.of(new ItemRequestDtoToReturn(), new ItemRequestDtoToReturn());
        when(itemRequestService.findAllByUserIdToPageable(1L, from, size)).thenReturn(itemRequestDtoToReturnList);

        List<ItemRequestDtoToReturn> actualItemRequestDtoToReturnList = itemRequestController.findAllById(1L, from, size);

        assertEquals(itemRequestDtoToReturnList.size(), actualItemRequestDtoToReturnList.size());
    }
}
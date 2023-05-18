package ru.practicum.shareit.item_request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item_request.dto.ItemRequestDto;
import ru.practicum.shareit.item_request.dto.ItemRequestMapper;
import ru.practicum.shareit.item_request.model.ItemRequest;
import ru.practicum.shareit.item_request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.booking.dto.BookingDto.StatusDto.ALL;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTestIT {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemRequestService itemRequestService;

    User owner = new User(1L, "userName", "email@mail.com");
    User requestor = new User(1L, "requestorName", "requestorEmail@mail.com");
    ItemRequest request = new ItemRequest(1L, "requestDescription", requestor);

    @SneakyThrows
    @Test
    void create() {
        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(request);
        when(itemRequestService.create(anyLong(), any(ItemRequestDto.class))).thenReturn(itemRequestDto);

        String result = mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", owner.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemRequestService, times(1)).create(anyLong(), any(ItemRequestDto.class));
        assertEquals(objectMapper.writeValueAsString(itemRequestDto), result);
    }

    @SneakyThrows
    @Test
    void findAllByUserId() {
        Long userId = 1L;
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemRequestService).findAllByUserId(userId);
    }

    @SneakyThrows
    @Test
    void findById() {
        Long userId = 1L;
        Long requestId = 1L;
        mockMvc.perform(get("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemRequestService).findById(userId, requestId);
    }

    @SneakyThrows
    @Test
    void findAllById() {
        Long userId = 1L;
        Integer from = 0;
        Integer size = 10;
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", from.toString())
                        .param("size", size.toString()))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemRequestService).findAllByUserIdToPageable(userId, from, size);
    }
}
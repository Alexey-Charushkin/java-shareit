package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDtoToReturn;
import ru.practicum.shareit.request.dto.ItemRequestDtoToSave;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTestIT {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemRequestService itemRequestService;
    User owner = new User(0L, "userName", "email@mail.com");
    User requestor = new User(1L, "requestorName", "requestorEmail@mail.com");
    ItemRequest request = new ItemRequest(1L, "requestDescription", requestor, null, null);

    @SneakyThrows
    @Test
    void create() {
        ItemRequestDtoToReturn itemRequestDtoToReturn = ItemRequestMapper.toItemRequestDto(request);
        when(itemRequestService.create(anyLong(), any(ItemRequestDtoToSave.class))).thenReturn(itemRequestDtoToReturn);

        String result = mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", owner.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemRequestDtoToReturn)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemRequestService, times(1)).create(anyLong(), any(ItemRequestDtoToSave.class));
        assertEquals(objectMapper.writeValueAsString(itemRequestDtoToReturn), result);
    }

    @SneakyThrows
    @Test
    void findAllByUserId() {
        Long userId = 1L;
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemRequestService, times(1)).findAllByUserId(userId);
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

        verify(itemRequestService, times(1)).findAllByUserIdToPageable(userId, from, size);
    }
}
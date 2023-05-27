package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDtoToSave;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTestIt {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RequestClient requestClient;

    ItemRequestDtoToSave requestDtoToSave = new ItemRequestDtoToSave("save Description");

    @SneakyThrows
    @Test
    void create() {
        long userId = 1;
        ResponseEntity<Object> exceptedEntity = new ResponseEntity<>(requestDtoToSave, HttpStatus.OK);
        when(requestClient.create(anyLong(), any(ItemRequestDtoToSave.class))).thenReturn(exceptedEntity);

        String result = mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDtoToSave)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(requestClient, times(1)).create(anyLong(), any(ItemRequestDtoToSave.class));
        assertEquals(objectMapper.writeValueAsString(requestDtoToSave), result);
    }

    @SneakyThrows
    @Test
    void findAllByUserId() {
        Long userId = 1L;
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(requestClient, times(1)).findAllByUserId(userId);
    }

    @SneakyThrows
    @Test
    void findById() {
        long userId = 1;
        long requestId = 1;
        mockMvc.perform(get("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(requestClient, times(1)).findById(userId, requestId);
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

        verify(requestClient, times(1)).findAllByUserIdToPageable(userId, from, size);
    }


}
package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.comment.dto.CommentDto;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoToReturn;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTestIT {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemClient itemClient;

    UserDto owner = new UserDto(1L, "userName", "email@mail.com");
    UserDto booker = new UserDto(2L, "bookerName", "bookerEmil@mail.com");
    ItemRequestDtoToReturn request = new ItemRequestDtoToReturn(1L, "requestDescription",
            null, null);
    ItemDto item = new ItemDto(1L, "itemName", "itemDescription",
            true, request);
    ItemDto newItem = new ItemDto(1L, "updateItemName", "updateItemDescription",
            true, request);
    CommentDto comment = new CommentDto(1L, "comment text", item, booker, booker.getName(), LocalDateTime.now());


    @SneakyThrows
    @Test
    void create() {
        ItemDto exceptedItemDto = item;
        ResponseEntity<Object> exceptedEntity = new ResponseEntity<>(exceptedItemDto, HttpStatus.OK);
        when(itemClient.create(anyLong(), any(ItemDto.class))).thenReturn(exceptedEntity);

        String result = mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", owner.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(exceptedItemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemClient, times(1)).create(anyLong(), any(ItemDto.class));
        assertEquals(objectMapper.writeValueAsString(item), result);
    }

    @SneakyThrows
    @Test
    void create_whenXSharerUserIdISEmpty() {
        ItemDto exceptedItemDto = item;
        ResponseEntity<Object> exceptedEntity = new ResponseEntity<>(exceptedItemDto, HttpStatus.OK);
        when(itemClient.create(null, exceptedItemDto)).thenReturn(exceptedEntity);

        mockMvc.perform(post("/items")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(exceptedItemDto)))
                .andExpect(status().is4xxClientError());
    }

    @SneakyThrows
    @Test
    void update() {
        Long itemId = 1L;
        ItemDto newItemDto = newItem;
        ResponseEntity<Object> exceptedEntity = new ResponseEntity<>(newItemDto, HttpStatus.OK);
        when(itemClient.update(anyLong(), anyLong(), any(ItemDto.class))).thenReturn(exceptedEntity);

        String result = mockMvc.perform(patch("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", owner.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(newItemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemClient, times(1)).update(anyLong(), anyLong(), any(ItemDto.class));
        assertEquals(objectMapper.writeValueAsString(newItemDto), result);
    }

    @SneakyThrows
    @Test
    void getById() {
        long itemId = 1;
        mockMvc.perform(get("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", owner.getId()))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemClient, times(1)).getItemById(owner.getId(), itemId);
    }

    @SneakyThrows
    @Test
    void gelAllByUserId() {
        long userId = 1;
        Integer from = 0;
        Integer size = 10;
        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", from.toString())
                        .param("size", size.toString()))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemClient, times(1)).getAllItemsByUserId(userId, from, size);
    }

    @SneakyThrows
    @Test
    void deleteById() {
        long itemId = 1;
        mockMvc.perform(delete("/items/{itemId}", itemId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemClient).deleteById(itemId);
    }

    @SneakyThrows
    @Test
    void searchItems() {
        String query = "query Request";
        long userId = 1;
        Integer from = 0;
        Integer size = 10;
        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", userId)
                        .param("text", query)
                        .param("from", from.toString())
                        .param("size", size.toString()))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemClient).searchItems(userId, query, from, size);
    }

    @SneakyThrows
    @Test
    void searchItems_whenQueryIsNull() {
        String query = "";
        long userId = 1;
        Integer from = 0;
        Integer size = 10;
        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", userId)
                        .param("text", query)
                        .param("from", from.toString())
                        .param("size", size.toString()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void testCreateComment() {
        ResponseEntity<Object> exceptedEntity = new ResponseEntity<>(comment, HttpStatus.OK);
        when(itemClient.createComment(anyLong(), anyLong(), any(CommentDto.class))).thenReturn(exceptedEntity);

        String result = mockMvc.perform(post("/items/{itemId}/comment", item.getId())
                        .header("X-Sharer-User-Id", owner.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemClient, times(1)).createComment(anyLong(), anyLong(), any(CommentDto.class));
        assertEquals(objectMapper.writeValueAsString(comment), result);
    }
}
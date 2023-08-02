package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.service.CommentService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
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
    private ItemService itemService;
    @MockBean
    private CommentService commentService;

    User owner = new User(1L, "userName", "email@mail.com");
    User requestor = new User(2L, "requestorName", "requestorEmail@mail.com");
    ItemRequest request = new ItemRequest(1L, "requestDescription", requestor, null, null);
    Item item = new Item(1L, "itemName", "itemDescription",
            true, owner, request);
    Item item2 = new Item(2L, "itemName2", "itemDescription2",
            true, owner, request);

    @SneakyThrows
    @Test
    void create() {
        Item item = new Item(1L, "itemName", "itemDescription",
                true, owner, request);
        ItemDto itemDto = ItemMapper.toItemDto(item);
        when(itemService.create(anyLong(), any(ItemDto.class))).thenReturn(itemDto);

        String result = mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", owner.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemService, times(1)).create(anyLong(), any(ItemDto.class));
        assertEquals(objectMapper.writeValueAsString(itemDto), result);

    }

    @SneakyThrows
    @Test
    void create_whenXSharerUserIdISEmpty() {
        Item item = new Item(1L, "itemName", "itemDescription",
                true, owner, request);
        ItemDto itemDto = ItemMapper.toItemDto(item);
        when(itemService.create(anyLong(), any(ItemDto.class))).thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().is5xxServerError());
    }

    @SneakyThrows
    @Test
    void update() {
        Long itemId = 1L;
        Item item = new Item(1L, "updateItemName", "updateItemDescription",
                true, owner, request);
        ItemDto itemDto = ItemMapper.toItemDto(item);
        when(itemService.update(anyLong(), any(ItemDto.class))).thenReturn(itemDto);

        String result = mockMvc.perform(patch("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", owner.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemService, times(1)).update(anyLong(), any(ItemDto.class));
        assertEquals(objectMapper.writeValueAsString(itemDto), result);
    }

    @SneakyThrows
    @Test
    void getById() {
        Long itemId = 1L;
        mockMvc.perform(get("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", owner.getId()))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemService).getItemById(owner.getId(), itemId);
    }

    @SneakyThrows
    @Test
    void gelAllByUserId() {
        Long userId = 1L;
        Integer from = 0;
        Integer size = 10;
        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", from.toString())
                        .param("size", size.toString()))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemService).getAllItemsByUserId(userId, from, size);
    }

    @SneakyThrows
    @Test
    void deleteById() {
        Long itemId = 1L;
        mockMvc.perform(delete("/items/{itemId}", itemId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemService).deleteById(itemId);
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

        verify(itemService).searchItems(query, from, size);
    }

    @SneakyThrows
    @Test
    void testCreateComment() {
        Item item = new Item(1L, "itemName", "itemDescription",
                true, owner, request);
        ItemDto itemDto = ItemMapper.toItemDto(item);
        Comment comment = new Comment(1L, "Comment text", item, owner, LocalDateTime.now());
        CommentDto commentDto = CommentMapper.toCommentDto(comment);

        when(commentService.create(anyLong(), anyLong(), any(CommentDto.class))).thenReturn(commentDto);


        String result = mockMvc.perform(post("/items/{itemId}/comment", item.getId())
                        .header("X-Sharer-User-Id", owner.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(commentService, times(1)).create(anyLong(), anyLong(), any(CommentDto.class));
        assertEquals(objectMapper.writeValueAsString(commentDto), result);
    }
}
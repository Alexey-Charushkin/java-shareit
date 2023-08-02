package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.comment.dto.CommentDto;

import ru.practicum.shareit.item.dto.ItemDto;

import ru.practicum.shareit.request.dto.ItemRequestDtoToReturn;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {
    @Mock
    private ItemClient itemClient;

    @InjectMocks
    private ItemController itemController;

    UserDto owner = new UserDto(1L, "userName", "email@mail.com");

    UserDto booker = new UserDto(2L, "bookerName", "bookerEmil@mail.com");
    ItemRequestDtoToReturn request = new ItemRequestDtoToReturn(1L, "requestDescription",
            null, null);
    ItemDto item = new ItemDto(1L, "itemName", "itemDescription",
            true, request);
    ItemDto newItem = new ItemDto(1L, "updateItemName", "updateItemDescription",
            true, request);
    CommentDto comment = new CommentDto(1L, "comment text", item, booker, booker.getName(), LocalDateTime.now());

    @Test
    void create() {
        long userId = 1;
        ItemDto exceptedItemDto = item;
        ResponseEntity<Object> exceptedEntity = new ResponseEntity<>(exceptedItemDto, HttpStatus.OK);
        when(itemClient.create(owner.getId(), exceptedItemDto)).thenReturn(exceptedEntity);

        ResponseEntity<Object> response = itemController.create(owner.getId(), exceptedItemDto);

        verify(itemClient, times(1)).create(userId, exceptedItemDto);
        assertEquals(exceptedEntity.getStatusCode(), response.getStatusCode());
        assertEquals(exceptedEntity.getClass(), response.getClass());
        assertEquals(exceptedEntity.getHeaders(), response.getHeaders());
        assertEquals(exceptedEntity.getBody(), response.getBody());
    }

    @Test
    void update() {
        long itemId = 1;
        ItemDto newItemDto = newItem;
        ResponseEntity<Object> exceptedEntity = new ResponseEntity<>(newItemDto, HttpStatus.OK);
        when(itemClient.update(owner.getId(), itemId, newItemDto)).thenReturn(exceptedEntity);

        ResponseEntity<Object> response = itemController.update(owner.getId(), itemId, newItemDto);

        verify(itemClient, times(1)).update(owner.getId(), itemId, newItemDto);
        assertEquals(exceptedEntity.getStatusCode(), response.getStatusCode());
        assertEquals(exceptedEntity.getClass(), response.getClass());
        assertEquals(exceptedEntity.getHeaders(), response.getHeaders());
        assertEquals(exceptedEntity.getBody(), response.getBody());
    }

    @Test
    void getById() {
        long itemId = 1;
        ResponseEntity<Object> exceptedEntity = new ResponseEntity<>(item, HttpStatus.OK);
        when(itemClient.getItemById(owner.getId(), item.getId())).thenReturn(exceptedEntity);

        ResponseEntity<Object> response = itemController.getById(owner.getId(), itemId);

        verify(itemClient, times(1)).getItemById(owner.getId(), itemId);
        assertEquals(exceptedEntity.getStatusCode(), response.getStatusCode());
        assertEquals(exceptedEntity.getClass(), response.getClass());
        assertEquals(exceptedEntity.getHeaders(), response.getHeaders());
        assertEquals(exceptedEntity.getBody(), response.getBody());
    }

    @Test
    void gelAllByUserId() {
        long userId = 1;
        ResponseEntity<Object> exceptedEntity = new ResponseEntity<>(item, HttpStatus.OK);
        when(itemClient.getAllItemsByUserId(userId, 0, 1)).thenReturn(exceptedEntity);

        ResponseEntity<Object> response = itemController.gelAllByUserId(userId, 0, 1);

        verify(itemClient, times(1)).getAllItemsByUserId(userId, 0, 1);
        assertEquals(exceptedEntity.getStatusCode(), response.getStatusCode());
        assertEquals(exceptedEntity.getClass(), response.getClass());
        assertEquals(exceptedEntity.getHeaders(), response.getHeaders());
        assertEquals(exceptedEntity.getBody(), response.getBody());
    }

    @Test
    void deleteById() {
        long itemId = 1;
        ResponseEntity<Object> exceptedEntity = new ResponseEntity<>(item, HttpStatus.OK);
        when(itemClient.getItemById(owner.getId(), item.getId())).thenReturn(exceptedEntity);

        ResponseEntity<Object> response = itemController.getById(owner.getId(), itemId);

        verify(itemClient, times(1)).getItemById(owner.getId(), itemId);
        assertEquals(exceptedEntity.getStatusCode(), response.getStatusCode());
        assertEquals(exceptedEntity.getClass(), response.getClass());
        assertEquals(exceptedEntity.getHeaders(), response.getHeaders());
        assertEquals(exceptedEntity.getBody(), response.getBody());

        itemController.deleteById(anyLong());

        ResponseEntity<Object> currentResponse = itemController.getById(anyLong(), anyLong());

        assertNull(currentResponse);
    }

    @Test
    void searchItems() {
        long userId = 1;
        String query = "searchQuery";
        ResponseEntity<Object> exceptedEntity = new ResponseEntity<>(item, HttpStatus.OK);

        when(itemClient.searchItems(userId, query, 0, 1)).thenReturn(exceptedEntity);

        ResponseEntity<Object> response = itemController.searchItems(userId, query, 0, 1);

        verify(itemClient, times(1)).searchItems(userId, query, 0, 1);
        assertEquals(exceptedEntity.getStatusCode(), response.getStatusCode());
        assertEquals(exceptedEntity.getClass(), response.getClass());
        assertEquals(exceptedEntity.getHeaders(), response.getHeaders());
        assertEquals(exceptedEntity.getBody(), response.getBody());
    }

    @Test
    void testCreate() {
        ResponseEntity<Object> exceptedEntity = new ResponseEntity<>(comment, HttpStatus.OK);
        when(itemClient.createComment(1L, 1L, comment)).thenReturn(exceptedEntity);

        ResponseEntity<Object> actualCommentDto = itemController.create(1L, 1L, comment);

        assertEquals(comment, actualCommentDto.getBody());
    }
}
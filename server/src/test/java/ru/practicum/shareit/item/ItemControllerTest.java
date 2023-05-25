package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {
    @Mock
    private ItemService itemService;

    @Mock
    private CommentService commentService;

    @InjectMocks
    private ItemController itemController;

    User owner = new User(0L, "userName", "email@mail.com");
    User requestor = new User(1L, "requestorName", "requestorEmail@mail.com");
    User booker = new User(2L, "bookerName", "bookerEmil@mail.com");
    ItemRequest request = new ItemRequest(0L, "requestDescription", requestor, null, null);
    Item item = new Item(0L, "itemName", "itemDescription",
            true, owner, request);
    ItemDto itemDto = ItemMapper.toItemDto(item);
    Comment comment = new Comment(0L, "comment text", item, booker, LocalDateTime.now());
    CommentDto commentDto = CommentMapper.toCommentDto(comment);
    List<Comment> commentList = List.of(comment, comment);

    @Test
    void create() {
        ItemDto exceptedItemDto = ItemMapper.toItemDto(item);

        when(itemService.create(owner.getId(), exceptedItemDto)).thenReturn(exceptedItemDto);

        ItemDto response = itemController.create(owner.getId(), exceptedItemDto);

        assertEquals(exceptedItemDto.getId(), response.getId());
        assertEquals(exceptedItemDto.getName(), response.getName());
        assertEquals(exceptedItemDto.getDescription(), response.getDescription());
        assertEquals(exceptedItemDto.getAvailable(), response.getAvailable());
    }

    @Test
    void update() {
        ItemDto exceptedItemDto = ItemMapper.toItemDto(item);
        Item newItem = new Item(0L, "newItemName", "newItemDescription",
                true, owner, request);
        ItemDto newItemDto = ItemMapper.toItemDto(newItem);
        when(itemService.update(owner.getId(), newItemDto))
                .thenReturn(newItemDto);

        ItemDto response = itemController.update(owner.getId(), exceptedItemDto.getId(), newItemDto);

        assertEquals(newItemDto.getId(), response.getId());
        assertEquals(newItemDto.getName(), response.getName());
        assertEquals(newItemDto.getDescription(), response.getDescription());
        assertEquals(newItemDto.getAvailable(), response.getAvailable());
    }

    @Test
    void getById() {
        Long id = 0L;
        when(itemService.getItemById(owner.getId(), item.getId())).thenReturn(ItemMapper.toItemDto(item));

        ItemDto response = itemController.getById(owner.getId(), id);

        assertEquals(itemDto.getId(), response.getId());
        assertEquals(itemDto.getName(), response.getName());
        assertEquals(itemDto.getDescription(), response.getDescription());
        assertEquals(itemDto.getAvailable(), response.getAvailable());
    }

    @Test
    void gelAllByUserId() {
        Long id = 1L;
        List<ItemDto> exceptedItemDtoList = List.of(new ItemDto(), new ItemDto());
        when(itemService.getAllItemsByUserId(id, 0, 1)).thenReturn(exceptedItemDtoList);

        List<ItemDto> response = itemController.gelAllByUserId(id, 0, 1);

        assertEquals(exceptedItemDtoList.size(), response.size());
    }

    @Test
    void deleteById() {
        ItemDto exceptedItemDto = ItemMapper.toItemDto(item);
        when(itemService.create(owner.getId(), exceptedItemDto)).thenReturn(exceptedItemDto);
        when(itemService.getItemById(anyLong(), anyLong())).thenReturn(null);
        ItemDto response = itemController.create(owner.getId(), exceptedItemDto);

        assertEquals(exceptedItemDto.getId(), response.getId());
        assertEquals(exceptedItemDto.getName(), response.getName());
        assertEquals(exceptedItemDto.getDescription(), response.getDescription());
        assertEquals(exceptedItemDto.getAvailable(), response.getAvailable());

        itemController.deleteById(anyLong());

        ItemDto currentResponse = itemController.getById(anyLong(), anyLong());

        assertNull(currentResponse);
    }

    @Test
    void searchItems() {
        Long id = 1L;
        String query = "searchQuery";
        List<ItemDto> exceptedItemDtoList = List.of(new ItemDto(), new ItemDto());
        when(itemService.searchItems(query, 0, 1)).thenReturn(exceptedItemDtoList);

        List<ItemDto> response = itemController.searchItems(query, 0, 1);

        assertEquals(exceptedItemDtoList.size(), response.size());
    }

    @Test
    void testCreate() {
        when(commentService.create(1L, 1L, commentDto)).thenReturn(commentDto);

        CommentDto actualCommentDto = itemController.create(1L, 1L, commentDto);

        assertEquals(commentDto, actualCommentDto);
    }
}
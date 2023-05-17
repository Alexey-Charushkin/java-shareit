package ru.practicum.shareit.comment.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.dao.CommentRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private CommentServiceImpl commentService;

    User owner = new User(0L, "userName", "email@mail.com");
    Item item = new Item(0L, "itemName", "itemDescription",
            true, owner, null);
    Item item2 = new Item(1L, "updateItemName", "updateItemDescription",
            true, owner, null);
    User booker = new User(2L, "bookerName", "bookerEmil@mail.com");
    Booking bookingToSave = new Booking(0L, LocalDateTime.now().minusMinutes(10), LocalDateTime.now().minusMinutes(5),
            item, booker, "APPROVED");
    Booking bookingToSave2 = new Booking(2L, LocalDateTime.now().minusMinutes(4), LocalDateTime.now().minusMinutes(1),
            item2, booker, "APPROVED");
    List<Booking> bookingList = new ArrayList<>(List.of(bookingToSave, bookingToSave2));
    Comment comment = new Comment(0L, "comment text", item, booker, LocalDateTime.now());
    CommentDto commentDto = CommentMapper.toCommentDto(comment);
    List<Comment> commentList = List.of(comment, comment);

    @Test
    void create_whenAllIsCorrect_thenSaveComment() {
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.ofNullable(item));
        when(bookingRepository.findByItemId(item.getId(),
                Sort.by(Sort.Direction.ASC, "end"))).thenReturn(bookingList);

        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDto actualCommentDto = commentService.create(booker.getId(), item.getId(), commentDto);

        verify(commentRepository, times(1)).save(any(Comment.class));
        assertEquals(actualCommentDto.getId(), comment.getId());
        assertEquals(actualCommentDto.getText(), comment.getText());
        assertEquals(actualCommentDto.getItem(), comment.getItem());
        assertEquals(actualCommentDto.getAuthor(), comment.getAuthor());
    }

    @Test
    void create_whenUserNotFound_thenNotFoundExceptionThrown() {
        when(userRepository.findById(booker.getId())).thenReturn(Optional.empty());

        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> commentService.create(booker.getId(), item.getId(), commentDto));
        assertEquals(notFoundException.getMessage(), "User not found.");
    }

    @Test
    void create_whenItemNotFound_thenNotFoundExceptionThrown() {
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.empty());

        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> commentService.create(booker.getId(), item.getId(), commentDto));
        assertEquals(notFoundException.getMessage(), "Item not found.");
    }

    @Test
    void create_whenUserIsNotBooker_thenBadRequestExceptionThrown() {
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> commentService.create(owner.getId(), item.getId(), commentDto));
        assertEquals(badRequestException.getMessage(), "User is not booker.");
    }

    @Test
    void findByItemId() {
        when(commentRepository.findByItemId(anyLong(), any(Sort.class))).thenReturn(commentList);

        List<Comment> actualComments = commentService.findByItemId(0L);

        verify(commentRepository, times(1)).findByItemId(anyLong(), any(Sort.class));
        assertEquals(actualComments.size(), commentList.size());
        assertEquals(actualComments.get(0).getId(), commentList.get(0).getId());
        assertEquals(actualComments.get(0).getText(), commentList.get(0).getText());
        assertEquals(actualComments.get(0).getAuthor(), commentList.get(0).getAuthor());
        assertEquals(actualComments.get(0).getItem(), commentList.get(0).getItem());
        assertEquals(actualComments.get(1).getId(), commentList.get(1).getId());
        assertEquals(actualComments.get(1).getText(), commentList.get(1).getText());
        assertEquals(actualComments.get(1).getAuthor(), commentList.get(1).getAuthor());
        assertEquals(actualComments.get(1).getItem(), commentList.get(1).getItem());
    }
}
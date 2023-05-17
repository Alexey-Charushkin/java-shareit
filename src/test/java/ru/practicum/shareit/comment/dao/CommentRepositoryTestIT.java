package ru.practicum.shareit.comment.dao;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item_request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql(scripts = {"/test-data.sql"})
class CommentRepositoryTestIT {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;
    User owner = new User(1L, "userName", "email@mail.com");
    User requestor = new User(2L, "requestorName", "requestorEmail@mail.com");
    ItemRequest request = new ItemRequest(1L, "requestDescription", requestor);
    Item item = new Item(1L, "itemName", "itemDescription",
            true, owner, request);
    Comment comment = new Comment(1L, "Comment text", item, owner, LocalDateTime.now());
    Comment comment2 = new Comment(2L, "Comment2 text", item, owner, LocalDateTime.now());
    Comment comment3 = new Comment(2L, "Comment3 text", item, owner, LocalDateTime.now());

    @Test
    void findByItemId() {
        List<Comment> actualComments = commentRepository.findByItemId(item.getId(), Sort.by(Sort.Direction.ASC, "id"));

        assertEquals(actualComments.size(), 3);
        assertEquals(actualComments.get(0).getText(), comment.getText());
        assertEquals(actualComments.get(1).getText(), comment2.getText());
        assertEquals(actualComments.get(2).getText(), comment3.getText());
    }
}
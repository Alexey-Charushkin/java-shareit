package ru.practicum.shareit.comment.dao;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
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
@ExtendWith(MockitoExtension.class)
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

    void setUp() {
        userRepository.save(User.builder()
                .name("userName")
                .email("userEmail@mail.com")
                .build());
        userRepository.save(User.builder()
                .name("userName2")
                .email("userEmail2@mail.com")
                .build());

        itemRepository.save(Item.builder()
                .name("itemName")
                .description("itemDescription")
                .available(true)
                .owner(owner)
                .request(null)
                .build());

        itemRepository.save(Item.builder()
                .name("itemName2")
                .description("itemDescription2")
                .available(true)
                .owner(owner)
                .request(null)
                .build());
        itemRepository.save(Item.builder()
                .name("itemName3")
                .description("itemDescription3")
                .available(true)
                .owner(requestor)
                .request(null)
                .build());

        commentRepository.save(Comment.builder()
                .author(requestor)
                .created(LocalDateTime.now())
                .item(item)
                .text("Comment text")
                .build());
        commentRepository.save(Comment.builder()
                .text("Comment2 text")
                .item(item)
                .author(owner)
                .created(LocalDateTime.now())
                .build());
        commentRepository.save(Comment.builder()
                .text("Comment3 text")
                .item(item)
                .author(owner)
                .created(LocalDateTime.now())
                .build());
    }


    @Test
    void findByItemId() {
        setUp();
        List<Comment> actualComments = commentRepository.findByItemId(item.getId(), Sort.by(Sort.Direction.ASC, "id"));

        assertEquals(actualComments.size(), 3);
        assertEquals(actualComments.get(0).getText(), comment.getText());
        assertEquals(actualComments.get(1).getText(), comment2.getText());
        assertEquals(actualComments.get(2).getText(), comment3.getText());
    }
}
package ru.practicum.shareit.item_request.dao;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item_request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql(scripts = {"/test-data.sql"})
class ItemRequestRepositoryTestIT {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    User owner = new User(1L, "userName", "email@mail.com");
    User requestor = new User(2L, "userName2", "email2@mail.com");
    ItemRequest request = new ItemRequest(1L, "requestDescription", requestor);
    ItemRequest request2 = new ItemRequest(2L, "requestDescription2", owner);

    Item item = new Item(1L, "itemName", "itemDescription",
            true, owner, request);
    Item item2 = new Item(2L, "itemName2", "itemDescription2",
            true, owner, request2);

    ItemDto itemDto = ItemMapper.toItemDto(item);
    ItemDto itemDto2 = ItemMapper.toItemDto(item2);
    List<ItemDto> itemDtoList = List.of(itemDto, itemDto2);

    @Test
    void findAllByRequestorId() {
        List<ItemRequest> itemRequestList = itemRequestRepository.findAllByRequestorId(1L,
                Sort.by(Sort.Direction.DESC, "id"));

        assertEquals(itemRequestList.size(), 1);
        assertEquals(itemRequestList.get(0).getRequestor().getName(), owner.getName());
    }

    @Test
    void findAllByRequestorIdNot() {
        int from = 0;
        int size = 1;
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable page = PageRequest.of(from, size, sort);
        List<ItemRequest> itemRequestList = itemRequestRepository.findAllByRequestorIdNot(2L,
                page);

        assertEquals(itemRequestList.size(), 1);
        assertEquals(itemRequestList.get(0).getRequestor().getName(), owner.getName());
    }
}
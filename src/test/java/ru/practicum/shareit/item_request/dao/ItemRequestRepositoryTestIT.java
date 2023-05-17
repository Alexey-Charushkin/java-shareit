package ru.practicum.shareit.item_request.dao;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item_request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql(scripts = {"/test-data.sql"})
class ItemRequestRepositoryTestIT {
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    User owner = new User(1L, "userName", "email@mail.com");

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
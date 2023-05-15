package ru.practicum.shareit.item.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item_request.dao.ItemRequestRepository;
import ru.practicum.shareit.item_request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRepositoryTestIT {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    User owner = new User(1L, "userName", "email@mail.com");
    User wrongOwner = new User(99L, "user99Name", "email99@mail.com");
    User requestor = new User(2L, "requestorName", "requestorEmail@mail.com");
    ItemRequest request = new ItemRequest(1L, "requestDescription", requestor);
//
//    ItemDto itemToSave = new ItemDto(0L, "itemName", "itemDescription",
//            true, request);
//    Item item = new Item(1L, "itemName", "itemDescription",
//            true, owner, request);
//    Item item2 = new Item(2L, "updateItemName", "updateItemDescription",
//            true, owner, request);

    @BeforeEach
    void setUp() {

        userRepository.save(User.builder()
                .name("userName")
                .email("userEmail@mail.com")
                .build());
        userRepository.save(User.builder()
                .name("userName2")
                .email("userEmail2@mail.com")
                .build());

        itemRequestRepository.save(ItemRequest.builder()
                .description("requestDescription")
                .requestor(requestor)
                .items(null)
                .created(LocalDateTime.now())
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
                .request(request)
                .build());
//        itemRepository.save(Item.builder()
//                .name("itemName3")
//                .description("itemDescription3")
//                .available(true)
//                .owner(requestor)
//                .request(request)
//                .build()
//        );
    }

    @AfterEach
    void tearDown() {
        itemRepository.deleteAll();
    }

    @Test
    void findByOwnerId_whenItemsFound_thenReturnListItem() {
        List<Item> itemList = itemRepository.findByOwnerId(owner.getId(), Sort.by(Sort.Direction.ASC, "id"));

        assertEquals(itemList.size(), 2);
        assertEquals(itemList.get(0).getOwner(), owner);
        assertEquals(itemList.get(1).getOwner(), owner);
    }

    @Test
    void findByOwnerId_whenItemsNotFound_thenReturnEmptyListItem() {

        List<Item> itemList = itemRepository.findByOwnerId(wrongOwner.getId(), Sort.by(Sort.Direction.ASC, "id"));

        assertEquals(itemList.size(), 0);
    }

    @Test
    void findByOwnerId_whenItemsFoundToPage_thenReturnList() {
        int from = 0;
        int size = 10;
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable page = PageRequest.of(from, size, sort);
        List<Item> itemList = itemRepository.findByOwnerId(owner.getId(), page);

        assertEquals(itemList.size(), 2);
        assertEquals(itemList.get(0).getOwner(), owner);
        assertEquals(itemList.get(1).getOwner(), owner);
    }

    @Test
    void findAllByRequestId() {

    }

    @Test
    void search() {
    }

    @Test
    void searchToPage() {
    }
}
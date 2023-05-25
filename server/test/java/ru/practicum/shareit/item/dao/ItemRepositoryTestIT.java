package java.ru.practicum.shareit.item.dao;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql(scripts = {"/test-data.sql"})
class ItemRepositoryTestIT {
    @Autowired
    private ItemRepository itemRepository;
    User owner = new User(1L, "userName", "email@mail.com");
    User requestor = new User(2L, "requestorName", "requestorEmail@mail.com");
    ItemRequest request = new ItemRequest(1L, "requestDescription", requestor, null, null);

    @Test
    void findByOwnerId_whenItemsFound_thenReturnListItem() {

        List<Item> itemList = itemRepository.findByOwnerId(1L, Sort.by(Sort.Direction.ASC, "id"));

        assertEquals(itemList.size(), 2);
        assertEquals(itemList.get(0).getOwner().getId(), owner.getId());
        assertEquals(itemList.get(1).getOwner().getId(), owner.getId());
    }

    @Test
    void findByOwnerId_whenItemsNotFound_thenReturnEmptyListItem() {
        List<Item> itemList = itemRepository.findByOwnerId(99L, Sort.by(Sort.Direction.ASC, "id"));

        assertEquals(itemList.size(), 0);
    }

    @Test
    void findByOwnerId_whenItemsFoundToPage_thenReturnList() {
        int from = 0;
        int size = 2;
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable page = PageRequest.of(from, size, sort);
        List<Item> itemList = itemRepository.findByOwnerId(1L, page);

        assertEquals(itemList.size(), 2);
        assertEquals(itemList.get(0).getOwner().getName(), owner.getName());
        assertEquals(itemList.get(1).getOwner().getName(), owner.getName());
    }

    @Test
    void findAllByRequestId_whenItemsFound_thenReturnListItem() {
        List<Item> itemList = itemRepository.findAllByRequestId(request.getId());

        assertEquals(itemList.size(), 2);
        assertEquals(itemList.get(0).getName(), "itemName2");
        assertEquals(itemList.get(1).getName(), "itemName3");
    }

    @Test
    void search() {
        List<Item> searchResult = itemRepository.search("itemn");

        assertEquals(searchResult.size(), 3);

        searchResult = itemRepository.search("tIoN");

        assertEquals(searchResult.size(), 3);

        searchResult = itemRepository.search("2");

        assertEquals(searchResult.size(), 1);
    }

    @Test
    void searchToPage() {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");

        Pageable page = PageRequest.of(0, 2, sort);
        List<Item> searchResult = itemRepository.searchToPage("itemn", page);

        assertEquals(searchResult.size(), 2);

        page = PageRequest.of(0, 1, sort);
        searchResult = itemRepository.searchToPage("tIoN", page);

        assertEquals(searchResult.size(), 1);

        page = PageRequest.of(0, 2, sort);
        searchResult = itemRepository.searchToPage("2", page);

        assertEquals(searchResult.size(), 1);
    }
}
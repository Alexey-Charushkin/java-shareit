package ru.practicum.shareit.booking.dao;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item_request.dao.ItemRequestRepository;
import ru.practicum.shareit.item_request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookingRepositoryTestIT {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    User owner = new User(1L, "userName", "email@mail.com");
    User requestor = new User(2L, "requestorName", "requestorEmail@mail.com");
    ItemRequest request = new ItemRequest(1L, "requestDescription", requestor);
    Item item = new Item(1L, "itemName", "itemDescription",
            true, owner, request);
    Item item2 = new Item(2L, "itemName2", "itemDescription2",
            true, owner, request);


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
        itemRepository.save(Item.builder()
                .name("itemName3")
                .description("itemDescription3")
                .available(true)
                .owner(requestor)
                .request(request)
                .build());

        bookingRepository.save(Booking.builder()
                .start(LocalDateTime.now().plusMinutes(1))
                .end(LocalDateTime.now().plusMinutes(5))
                .item(item)
                .booker(owner)
                .status(Booking.Status.WAITING)
                .build());

        bookingRepository.save(Booking.builder()
                .start(LocalDateTime.now().plusMinutes(6))
                .end(LocalDateTime.now().plusMinutes(10))
                .item(item)
                .booker(owner)
                .status(Booking.Status.APPROVED)
                .build());

        bookingRepository.save(Booking.builder()
                .start(LocalDateTime.now().plusMinutes(11))
                .end(LocalDateTime.now().plusMinutes(15))
                .item(item2)
                .booker(requestor)
                .status(Booking.Status.REJECTED)
                .build());

        bookingRepository.save(Booking.builder()
                .start(LocalDateTime.now().plusMinutes(16))
                .end(LocalDateTime.now().plusMinutes(20))
                .item(item)
                .booker(requestor)
                .status(Booking.Status.APPROVED)
                .build());

    }

    @Order(1)
    @Rollback(value = false)
    @Test
    void findByItemId() {
        setUp();
        List<Booking> bookingList = bookingRepository.findByItemId(1L,
                Sort.by(Sort.Direction.ASC, "end"));

        assertEquals(bookingList.size(), 3);
        assertEquals(bookingList.get(0).getItem(), item);
        assertEquals(bookingList.get(1).getItem(), item);
        assertEquals(bookingList.get(2).getItem(), item);
    }

    @Test
    void findByItemIdAndStatus() {
        List<Booking> bookingList = bookingRepository.findByItemIdAndStatus(1L,
                Booking.Status.APPROVED, Sort.by(Sort.Direction.ASC, "end"));

        assertEquals(bookingList.size(), 2);
        assertEquals(bookingList.get(0).getItem().getName(), item.getName());
        assertEquals(bookingList.get(1).getItem().getName(), item.getName());
    }

    @Test
    void findByBookerId() {
        List<Booking> bookingList = bookingRepository.findByBookerId(2L,
                Sort.by(Sort.Direction.ASC, "end"));

        assertEquals(bookingList.size(), 2);
        assertEquals(bookingList.get(0).getItem().getName(), item2.getName());
        assertEquals(bookingList.get(1).getItem().getName(), item.getName());
    }

    @Test
    void testFindByBookerIdToPage() {
        int from = 0;
        int size = 1;
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable page = PageRequest.of(from, size, sort);
        List<Booking> bookingList = bookingRepository.findByBookerId(1L,
                page);

        assertEquals(bookingList.size(), 1);
        assertEquals(bookingList.get(0).getItem().getName(), item.getName());
    }

    @Test
    void findByBookerIdAndStatus() {
        List<Booking> bookingList = bookingRepository.findByBookerIdAndStatus(2L,
                Booking.Status.REJECTED, Sort.by(Sort.Direction.ASC, "end"));

        assertEquals(bookingList.size(), 1);
        assertEquals(bookingList.get(0).getItem().getName(), item2.getName());
    }

    @Test
    void testFindByBookerIdAndStatusToPage() {
        int from = 0;
        int size = 1;
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable page = PageRequest.of(from, size, sort);

        List<Booking> bookingList = bookingRepository.findByBookerIdAndStatus(2L,
                Booking.Status.APPROVED, page);

        assertEquals(bookingList.size(), 1);
        assertEquals(bookingList.get(0).getItem().getName(), item.getName());
    }

    @Test
    void findByBooker_IdAndEndIsBefore() {
        List<Booking> bookingList = bookingRepository.findByBooker_IdAndEndIsBefore(1L,
                LocalDateTime.now().plusMinutes(16), Sort.by(Sort.Direction.ASC, "end"));

        assertEquals(bookingList.size(), 2);
        assertEquals(bookingList.get(0).getItem().getName(), item.getName());
        assertEquals(bookingList.get(1).getItem().getName(), item.getName());
    }

    @Test
    void testFindByBooker_IdAndEndIsBeforeToPage() {
        int from = 0;
        int size = 1;
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable page = PageRequest.of(from, size, sort);

        List<Booking> bookingList = bookingRepository.findByBooker_IdAndEndIsBefore(1L,
                LocalDateTime.now().plusMinutes(16), page);

        assertEquals(bookingList.size(), 1);
        assertEquals(bookingList.get(0).getItem().getName(), item.getName());
    }


    @Test
    void findByBooker_IdAndStartIsAfter() {
        List<Booking> bookingList = bookingRepository.findByBooker_IdAndStartIsAfter(1L,
                LocalDateTime.now().plusMinutes(2), Sort.by(Sort.Direction.ASC, "end"));

        assertEquals(bookingList.size(), 1);
        assertEquals(bookingList.get(0).getItem().getName(), item.getName());
    }

    @Test
    void FindByBooker_IdAndStartIsAfterToPage() {
        int from = 3;
        int size = 1;
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable page = PageRequest.of(from, size, sort);

        List<Booking> bookingList = bookingRepository.findByBooker_IdAndStartIsAfter(1L,
                LocalDateTime.now().plusMinutes(16), page);

        assertEquals(bookingList.size(), 0);
    }

    @Test
    void findByBooker_IdAndStartIsBeforeAndEndIsAfter() {
        List<Booking> bookingList = bookingRepository.findByBooker_IdAndStartIsBeforeAndEndIsAfter(1L,
                LocalDateTime.now().plusMinutes(2), LocalDateTime.now().plusMinutes(4), Sort.by(Sort.Direction.ASC, "end"));

        assertEquals(bookingList.size(), 1);
        assertEquals(bookingList.get(0).getItem().getName(), item.getName());
    }

    @Test
    void testFindByBooker_IdAndStartIsBeforeAndEndIsAfterToPage() {
        int from = 3;
        int size = 1;
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable page = PageRequest.of(from, size, sort);

        List<Booking> bookingList = bookingRepository.findByBooker_IdAndStartIsBeforeAndEndIsAfter(1L,
                LocalDateTime.now().plusMinutes(2), LocalDateTime.now().plusMinutes(4), Sort.by(Sort.Direction.ASC, "end"));

        assertEquals(bookingList.size(), 1);
        assertEquals(bookingList.get(0).getItem().getName(), item.getName());
    }

    @Test
    void findByOwnerId() {
        List<Booking> bookingList = bookingRepository.findByOwnerId(1L,
                Sort.by(Sort.Direction.ASC, "end"));

        assertEquals(bookingList.size(), 4);
        assertEquals(bookingList.get(0).getItem().getName(), item.getName());
        assertEquals(bookingList.get(1).getItem().getName(), item.getName());
        assertEquals(bookingList.get(2).getItem().getName(), item2.getName());
        assertEquals(bookingList.get(3).getItem().getName(), item.getName());
    }

    @Test
    void testFindByOwnerIdToPage() {
        int from = 3;
        int size = 1;
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable page = PageRequest.of(from, size, sort);

        List<Booking> bookingList = bookingRepository.findByOwnerId(1L,
                page);

        assertEquals(bookingList.size(), 1);
        assertEquals(bookingList.get(0).getItem().getName(), item.getName());
    }

    @Test
    void findByOwnerIdAndStatus() {
        List<Booking> bookingList = bookingRepository.findByOwnerIdAndStatus(1L,
                Booking.Status.REJECTED, Sort.by(Sort.Direction.ASC, "end"));

        assertEquals(bookingList.size(), 1);
        assertEquals(bookingList.get(0).getItem().getName(), item2.getName());
    }

    @Test
    void testFindByOwnerIdAndStatus() {
        int from = 0;
        int size = 1;
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable page = PageRequest.of(from, size, sort);

        List<Booking> bookingList = bookingRepository.findByOwnerIdAndStatus(1L,
                Booking.Status.REJECTED, page);

        assertEquals(bookingList.size(), 1);
        assertEquals(bookingList.get(0).getItem().getName(), item2.getName());
    }

    @Test
    void findByOwnerIdAndEndBefore() {
        List<Booking> bookingList = bookingRepository.findByOwnerIdAndEndBefore(1L,
                LocalDateTime.now().plusMinutes(16), Sort.by(Sort.Direction.ASC, "end"));

        assertEquals(bookingList.size(), 3);
        assertEquals(bookingList.get(0).getItem().getName(), item.getName());
        assertEquals(bookingList.get(1).getItem().getName(), item.getName());
        assertEquals(bookingList.get(2).getItem().getName(), item2.getName());
    }

    @Test
    void FindByOwnerIdAndEndBeforeToPage() {
        int from = 1;
        int size = 2;
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable page = PageRequest.of(from, size, sort);
        List<Booking> bookingList = bookingRepository.findByOwnerIdAndEndBefore(1L,
                LocalDateTime.now().plusMinutes(16), page);

        assertEquals(bookingList.size(), 1);
        assertEquals(bookingList.get(0).getItem().getName(), item2.getName());
    }

    @Test
    void findByOwnerIdAndSAndStartIsAfter() {
        List<Booking> bookingList = bookingRepository.findByOwnerIdAndSAndStartIsAfter(1L,
                LocalDateTime.now().plusMinutes(2), Sort.by(Sort.Direction.ASC, "end"));

        assertEquals(bookingList.size(), 3);
        assertEquals(bookingList.get(0).getItem().getName(), item.getName());
        assertEquals(bookingList.get(1).getItem().getName(), item2.getName());
        assertEquals(bookingList.get(2).getItem().getName(), item.getName());
    }

    @Test
    void FindByOwnerIdAndSAndStartIsAfterToPage() {
        int from = 1;
        int size = 2;
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable page = PageRequest.of(from, size, sort);
        List<Booking> bookingList = bookingRepository.findByOwnerIdAndSAndStartIsAfter(1L,
                LocalDateTime.now().plusMinutes(2), page);

        assertEquals(bookingList.size(), 1);
        assertEquals(bookingList.get(0).getItem().getName(), item.getName());
    }

    @Test
    void findByOwnerIdAndStartIsBeforeAndEndIsAfter() {
        List<Booking> bookingList = bookingRepository.findByOwnerIdAndStartIsBeforeAndEndIsAfter(1L,
                LocalDateTime.now().plusMinutes(2), LocalDateTime.now().plusMinutes(4), Sort.by(Sort.Direction.ASC, "end"));

        assertEquals(bookingList.size(), 1);
        assertEquals(bookingList.get(0).getItem().getName(), item.getName());
    }

    @Test
    void testFindByOwnerIdAndStartIsBeforeAndEndIsAfter() {
        int from = 0;
        int size = 1;
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable page = PageRequest.of(from, size, sort);
        List<Booking> bookingList = bookingRepository.findByOwnerIdAndStartIsBeforeAndEndIsAfter(1L,
                LocalDateTime.now().plusMinutes(2), LocalDateTime.now().plusMinutes(4), page);

        assertEquals(bookingList.size(), 1);
        assertEquals(bookingList.get(0).getItem().getName(), item.getName());
    }
}
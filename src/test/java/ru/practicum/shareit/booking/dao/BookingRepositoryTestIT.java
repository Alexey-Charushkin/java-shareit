package ru.practicum.shareit.booking.dao;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item_request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Sql(scripts = {"/test-data.sql"})
class BookingRepositoryTestIT {
    @Autowired
    private BookingRepository bookingRepository;
    User owner = new User(1L, "userName", "email@mail.com");
    User requestor = new User(2L, "requestorName", "requestorEmail@mail.com");
    ItemRequest request = new ItemRequest(1L, "requestDescription", requestor);
    Item item = new Item(1L, "itemName", "itemDescription",
            true, owner, request);
    Item item2 = new Item(2L, "itemName2", "itemDescription2",
            true, owner, request);

    @Test
    void findByItemId() {
        List<Booking> bookingList = bookingRepository.findByItemId(1L,
                Sort.by(Sort.Direction.ASC, "end"));

        assertEquals(bookingList.size(), 3);
        assertEquals(bookingList.get(0).getItem().getName(), item.getName());
        assertEquals(bookingList.get(1).getItem().getName(), item.getName());
        assertEquals(bookingList.get(2).getItem().getName(), item.getName());
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
    void findByBooker_IdAndStartIsAfterToPage() {
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
        int from = 0;
        int size = 3;
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable page = PageRequest.of(from, size, sort);

        List<Booking> bookingList = bookingRepository.findByBooker_IdAndStartIsBeforeAndEndIsAfter(1L,
                LocalDateTime.now().plusMinutes(2), LocalDateTime.now().plusMinutes(4), page);

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
    void findByOwnerIdAndEndBeforeToPage() {
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
    void findByOwnerIdAndSAndStartIsAfterToPage() {
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
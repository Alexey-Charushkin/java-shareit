package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item_request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    User owner = new User(0L, "userName", "email@mail.com");
    User wrongOwner = new User(99L, "user99Name", "email99@mail.com");
    User requestor = new User(1L, "requestorName", "requestorEmail@mail.com");
    User booker = new User(2L, "bookerName", "bookerEmil@mail.com");
    ItemRequest request = new ItemRequest(0L, "requestDescription", requestor, null, null);
    Item item = new Item(0L, "itemName", "itemDescription",
            true, owner, request);
    Booking bookingToSave = new Booking(0L, LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusMinutes(5),
            item, booker, "WAITING");

    @Test
    void create() {
        BookingDto exceptedBookingDto = BookingMapper.toBookingDto(bookingToSave);

        when(bookingService.create(owner.getId(), exceptedBookingDto)).thenReturn(exceptedBookingDto);

        BookingDto response = bookingController.create(owner.getId(), exceptedBookingDto);

        assertEquals(exceptedBookingDto.getId(), response.getId());
        assertEquals(exceptedBookingDto.getItem(), response.getItem());
        assertEquals(exceptedBookingDto.getBooker(), response.getBooker());
        assertEquals(exceptedBookingDto.getStatus(), response.getStatus());
    }

    @Test
    void approveBooking() {
        BookingDto exceptedBookingDto = BookingMapper.toBookingDto(bookingToSave);
        when(bookingService.approveBooking(1L, 1L, true))
                .thenReturn(exceptedBookingDto);

        BookingDto response = bookingController.approveBooking(1L, 1L, true);

        assertEquals(exceptedBookingDto.getId(), response.getId());
        assertEquals(exceptedBookingDto.getItem(), response.getItem());
        assertEquals(exceptedBookingDto.getBooker(), response.getBooker());
        assertEquals(exceptedBookingDto.getStatus(), response.getStatus());
    }

    @Test
    void findByBookingId() {
        BookingDto exceptedBookingDto = BookingMapper.toBookingDto(bookingToSave);
        when(bookingService.findByBookingId(1L, 1L)).thenReturn(exceptedBookingDto);

        BookingDto response = bookingController.findByBookingId(1L, 1L);

        assertEquals(exceptedBookingDto.getId(), response.getId());
        assertEquals(exceptedBookingDto.getItem(), response.getItem());
        assertEquals(exceptedBookingDto.getBooker(), response.getBooker());
        assertEquals(exceptedBookingDto.getStatus(), response.getStatus());
    }

    @Test
    void gelAllBookingsByUserId() {
        List<BookingDto> exceptedBookingDtoList = List.of(new BookingDto(), new BookingDto());
        when(bookingService.getAllBookingsByUserId(1L, BookingDto.StatusDto.ALL, 0, 1))
                .thenReturn(exceptedBookingDtoList);

        List<BookingDto> actualBookingDtoList = bookingController
                .gelAllBookingsByUserId(1L, "ALL", 0, 1);

        assertEquals(exceptedBookingDtoList.size(), actualBookingDtoList.size());
    }

    @Test
    void gelAllBookingsByOwnerId() {
        List<BookingDto> exceptedBookingDtoList = List.of(new BookingDto(), new BookingDto());
        when(bookingService.getAllBookingsByOwnerId(1L, BookingDto.StatusDto.ALL, 0, 1))
                .thenReturn(exceptedBookingDtoList);

        List<BookingDto> actualBookingDtoList = bookingController
                .gelAllBookingsByOwnerId(1L, "ALL", 0, 1);

        assertEquals(exceptedBookingDtoList.size(), actualBookingDtoList.size());
    }
}
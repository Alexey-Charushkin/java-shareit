package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookItemResponseDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
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
        BookItemResponseDto exceptedBookItemResponseDto = BookingMapper.toBookingDto(bookingToSave);

        when(bookingService.create(owner.getId(), exceptedBookItemResponseDto)).thenReturn(exceptedBookItemResponseDto);

        BookItemResponseDto response = bookingController.create(owner.getId(), exceptedBookItemResponseDto);

        assertEquals(exceptedBookItemResponseDto.getId(), response.getId());
        assertEquals(exceptedBookItemResponseDto.getItem(), response.getItem());
        assertEquals(exceptedBookItemResponseDto.getBooker(), response.getBooker());
        assertEquals(exceptedBookItemResponseDto.getStatus(), response.getStatus());
    }

    @Test
    void approveBooking() {
        BookItemResponseDto exceptedBookItemResponseDto = BookingMapper.toBookingDto(bookingToSave);
        when(bookingService.approveBooking(1L, 1L, true))
                .thenReturn(exceptedBookItemResponseDto);

        BookItemResponseDto response = bookingController.approveBooking(1L, 1L, true);

        assertEquals(exceptedBookItemResponseDto.getId(), response.getId());
        assertEquals(exceptedBookItemResponseDto.getItem(), response.getItem());
        assertEquals(exceptedBookItemResponseDto.getBooker(), response.getBooker());
        assertEquals(exceptedBookItemResponseDto.getStatus(), response.getStatus());
    }

    @Test
    void findByBookingId() {
        BookItemResponseDto exceptedBookItemResponseDto = BookingMapper.toBookingDto(bookingToSave);
        when(bookingService.findByBookingId(1L, 1L)).thenReturn(exceptedBookItemResponseDto);

        BookItemResponseDto response = bookingController.findByBookingId(1L, 1L);

        assertEquals(exceptedBookItemResponseDto.getId(), response.getId());
        assertEquals(exceptedBookItemResponseDto.getItem(), response.getItem());
        assertEquals(exceptedBookItemResponseDto.getBooker(), response.getBooker());
        assertEquals(exceptedBookItemResponseDto.getStatus(), response.getStatus());
    }

    @Test
    void gelAllBookingsByUserId() {
        List<BookItemResponseDto> exceptedBookItemResponseDtoList = List.of(new BookItemResponseDto(), new BookItemResponseDto());
        when(bookingService.getAllBookingsByUserId(1L, BookItemResponseDto.StatusDto.ALL, 0, 1))
                .thenReturn(exceptedBookItemResponseDtoList);

        List<BookItemResponseDto> actualBookItemResponseDtoList = bookingController
                .gelAllBookingsByUserId(1L, "ALL", 0, 1);

        assertEquals(exceptedBookItemResponseDtoList.size(), actualBookItemResponseDtoList.size());
    }

    @Test
    void gelAllBookingsByOwnerId() {
        List<BookItemResponseDto> exceptedBookItemResponseDtoList = List.of(new BookItemResponseDto(), new BookItemResponseDto());
        when(bookingService.getAllBookingsByOwnerId(1L, BookItemResponseDto.StatusDto.ALL, 0, 1))
                .thenReturn(exceptedBookItemResponseDtoList);

        List<BookItemResponseDto> actualBookItemResponseDtoList = bookingController
                .gelAllBookingsByOwnerId(1L, "ALL", 0, 1);

        assertEquals(exceptedBookItemResponseDtoList.size(), actualBookItemResponseDtoList.size());
    }
}
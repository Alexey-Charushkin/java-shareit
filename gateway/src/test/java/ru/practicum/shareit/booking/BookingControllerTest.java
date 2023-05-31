package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Mock
    private BookingClient bookingClient;

    @InjectMocks
    private BookingController bookingController;

    UserDto owner = new UserDto(1L, "userName", "email@mail.com");
    BookItemRequestDto bookingToSave = new BookItemRequestDto(0, LocalDateTime.now().plusMinutes(1),
            LocalDateTime.now().plusMinutes(5));

    @Test
    void create() {
        ResponseEntity<Object> exceptedEntity = new ResponseEntity<>(bookingToSave, HttpStatus.OK);
        when(bookingClient.create(owner.getId(), bookingToSave)).thenReturn(exceptedEntity);

        ResponseEntity<Object> response = bookingController.create(owner.getId(), bookingToSave);

        verify(bookingClient, times(1)).create(owner.getId(), bookingToSave);
        assertEquals(exceptedEntity.getStatusCode(), response.getStatusCode());
        assertEquals(exceptedEntity.getClass(), response.getClass());
        assertEquals(exceptedEntity.getHeaders(), response.getHeaders());
        assertEquals(exceptedEntity.getBody(), response.getBody());
    }

    @Test
    void approveBooking() {
        ResponseEntity<Object> exceptedEntity = new ResponseEntity<>(bookingToSave, HttpStatus.OK);
        when(bookingClient.approveBooking(1L, 1L, true))
                .thenReturn(exceptedEntity);

        ResponseEntity<Object> response = bookingController.approveBooking(1L, 1L, true);

        verify(bookingClient, times(1)).approveBooking(1L, 1L, true);
        assertEquals(exceptedEntity.getStatusCode(), response.getStatusCode());
        assertEquals(exceptedEntity.getClass(), response.getClass());
        assertEquals(exceptedEntity.getHeaders(), response.getHeaders());
        assertEquals(exceptedEntity.getBody(), response.getBody());
    }

    @Test
    void findByBookingId() {
        ResponseEntity<Object> exceptedEntity = new ResponseEntity<>(bookingToSave, HttpStatus.OK);
        when(bookingClient.findByBookingId(1L, 1L)).thenReturn(exceptedEntity);

        ResponseEntity<Object> response = bookingController.findByBookingId(1L, 1L);

        verify(bookingClient, times(1)).findByBookingId(1L, 1L);
        assertEquals(exceptedEntity.getStatusCode(), response.getStatusCode());
        assertEquals(exceptedEntity.getClass(), response.getClass());
        assertEquals(exceptedEntity.getHeaders(), response.getHeaders());
        assertEquals(exceptedEntity.getBody(), response.getBody());
    }

    @Test
    void gelAllBookingsByUserId() {
        ResponseEntity<Object> exceptedEntity = new ResponseEntity<>(bookingToSave, HttpStatus.OK);
        when(bookingClient.getAllBookingsByUserId(1L, BookingState.ALL, 0, 1))
                .thenReturn(exceptedEntity);

        ResponseEntity<Object> response = bookingController
                .getAllBookingsByUserId(1L, "ALL", 0, 1);

        verify(bookingClient, times(1)).getAllBookingsByUserId(1L, BookingState.ALL,
                0, 1);
        assertEquals(exceptedEntity.getStatusCode(), response.getStatusCode());
        assertEquals(exceptedEntity.getClass(), response.getClass());
        assertEquals(exceptedEntity.getHeaders(), response.getHeaders());
        assertEquals(exceptedEntity.getBody(), response.getBody());
    }

    @Test
    void gelAllBookingsByOwnerId() {
        ResponseEntity<Object> exceptedEntity = new ResponseEntity<>(bookingToSave, HttpStatus.OK);
        when(bookingClient.getAllBookingsByOwnerId(1L, BookingState.ALL, 0, 1))
                .thenReturn(exceptedEntity);

        ResponseEntity<Object> response = bookingController
                .getAllBookingsByOwnerId(1L, "ALL", 0, 1);

        verify(bookingClient, times(1)).getAllBookingsByOwnerId(1L, BookingState.ALL,
                0, 1);
        assertEquals(exceptedEntity.getStatusCode(), response.getStatusCode());
        assertEquals(exceptedEntity.getClass(), response.getClass());
        assertEquals(exceptedEntity.getHeaders(), response.getHeaders());
        assertEquals(exceptedEntity.getBody(), response.getBody());
    }
}
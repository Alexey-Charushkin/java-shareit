package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item_request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.booking.dto.BookingDto.StatusDto.ALL;
import static ru.practicum.shareit.booking.dto.BookingDto.StatusDto.APPROVED;

@WebMvcTest(BookingController.class)
class BookingControllerTestIT {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookingService bookingService;

    User owner = new User(1L, "userName", "email@mail.com");
    User requestor = new User(1L, "requestorName", "requestorEmail@mail.com");
    User booker = new User(2L, "bookerName", "bookerEmil@mail.com");
    ItemRequest request = new ItemRequest(1L, "requestDescription", requestor);
    Item item = new Item(0L, "itemName", "itemDescription",
            true, owner, request);
    Booking bookingToSave = new Booking(1L, LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusMinutes(5),
            item, booker, "WAITING");

    @SneakyThrows
    @Test
    void create() {
        Booking bookingToSave = new Booking(0L, LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusMinutes(5),
                item, booker, "WAITING");
        BookingDto bookingDtoToSave = BookingMapper.toBookingDto(bookingToSave);
        when(bookingService.create(anyLong(), any(BookingDto.class))).thenReturn(bookingDtoToSave);

        String result = mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", owner.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingDtoToSave)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(bookingService, times(1)).create(anyLong(), any(BookingDto.class));
        assertEquals(objectMapper.writeValueAsString(bookingDtoToSave), result);
    }

    @SneakyThrows
    @Test
    void approveBooking() {
        Long userId = 1L;
        Long bookingId = 1L;
        mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", owner.getId())
                        .param("approved",  "true"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(bookingService).approveBooking(userId, bookingId, true);
    }

    @SneakyThrows
    @Test
    void findByBookingId() {
        Long userId = 1L;
        mockMvc.perform(get("/bookings/{bookingId}", userId)
                        .header("X-Sharer-User-Id", owner.getId()))
                .andDo(print())
                .andExpect(status().isOk());

        verify(bookingService).findByBookingId(owner.getId(), bookingToSave.getId());
    }

    @SneakyThrows
    @Test
    void gelAllBookingsByUserId() {
        Long userId = 1L;
        Integer from = 0;
        Integer size = 10;
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", from.toString())
                        .param("size", size.toString()))
                .andDo(print())
                .andExpect(status().isOk());

        verify(bookingService).getAllBookingsByUserId(userId, ALL, from, size);
    }

    @SneakyThrows
    @Test
    void gelAllBookingsByOwnerId() {
        Long userId = 1L;
        Integer from = 0;
        Integer size = 10;
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", from.toString())
                        .param("size", size.toString()))
                .andDo(print())
                .andExpect(status().isOk());

        verify(bookingService).getAllBookingsByOwnerId(userId, ALL, from, size);
    }
}
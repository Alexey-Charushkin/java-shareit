package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.booking.dto.BookingState.ALL;

@WebMvcTest(BookingController.class)
class BookingControllerTestIt {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookingClient bookingClient;
    UserDto owner = new UserDto(1L, "userName", "email@mail.com");
    BookItemRequestDto bookingToSave = new BookItemRequestDto(0, LocalDateTime.now().plusMinutes(1),
            LocalDateTime.now().plusMinutes(5));

    @SneakyThrows
    @Test
    void create() {
        ResponseEntity<Object> exceptedEntity = new ResponseEntity<>(bookingToSave, HttpStatus.OK);
        when(bookingClient.create(anyLong(), any(BookItemRequestDto.class))).thenReturn(exceptedEntity);

        String result = mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", owner.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(exceptedEntity)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(bookingClient, times(1)).create(anyLong(), any(BookItemRequestDto.class));
        assertEquals(objectMapper.writeValueAsString(bookingToSave), result);
    }

    @SneakyThrows
    @Test
    void approveBooking() {
        Long userId = 1L;
        Long bookingId = 1L;
        mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", owner.getId())
                        .param("approved", "true"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(bookingClient, times(1)).approveBooking(userId, bookingId, true);
    }

    @SneakyThrows
    @Test
    void findByBookingId() {
        Long userId = 1L;
        mockMvc.perform(get("/bookings/{bookingId}", userId)
                        .header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(bookingClient, times(1)).findByBookingId(userId, 1L);
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

        verify(bookingClient, times(1)).getAllBookingsByUserId(userId, ALL, from, size);
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

        verify(bookingClient).getAllBookingsByOwnerId(userId, ALL, from, size);
    }
}
package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.exceptions.ErrorResponse;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @RequestBody @Valid BookItemRequestDto requestDto) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.create(userId, requestDto);
    }

    @PatchMapping("{bookingId}")
    public ResponseEntity<Object> approveBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId,
                                                 @RequestParam ("approved") boolean approved) {

        log.info("Patch X-Sharer-User-Id /:bookindId");
        return bookingClient.approveBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findByBookingId(@RequestHeader("X-Sharer-User-Id") long userId,
                                                  @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.findByBookingId(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllBookingsByUserId(@RequestHeader("X-Sharer-User-Id") long userId,
                                                         @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                         @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        try {
            BookingState state = BookingState.from(stateParam)
                    .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
            log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
            return bookingClient.getAllBookingsByUserId(userId, state, from, size);
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping("owner")
    public ResponseEntity<Object> getAllBookingsByOwnerId(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                          @RequestParam(value = "state", defaultValue = "ALL") String stateParam,
                                                          @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                          @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        try {
            BookingState state = BookingState.from(stateParam)
                    .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
            log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, ownerId, from, size);
            return bookingClient.getAllBookingsByOwnerId(ownerId, state, from, size);
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}

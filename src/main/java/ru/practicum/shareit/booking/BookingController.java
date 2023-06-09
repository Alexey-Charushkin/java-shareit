package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@Validated
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping()
    public BookingDto create(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody BookingDto bookingDto) {
        log.info("Post X-Sharer-User-Id");
        return bookingService.create(userId, bookingDto);
    }

    @PatchMapping("{bookingId}")
    public BookingDto approveBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId,
                                     @RequestParam("approved") Boolean approved) {
        log.info("Patch X-Sharer-User-Id /:bookindId");
        return bookingService.approveBooking(userId, bookingId, approved);
    }

    @GetMapping("{bookingId}")
    public BookingDto findByBookingId(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId) {
        log.info("Get X-Sharer-User-Id and bookingId");
        return bookingService.findByBookingId(userId, bookingId);
    }

    @GetMapping()
    public List<BookingDto> gelAllBookingsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestParam(value = "state", defaultValue = "ALL") String status,
                                                   @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get X-Sharer-User-Id, userId");
        return bookingService.getAllBookingsByUserId(userId, BookingDto.StatusDto.valueOf(status), from, size);
    }

    @GetMapping("owner")
    public List<BookingDto> gelAllBookingsByOwnerId(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                    @RequestParam(value = "state", defaultValue = "ALL") String status,
                                                    @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                    @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get X-Sharer-User-Id, ownerId");
        return bookingService.getAllBookingsByOwnerId(ownerId, BookingDto.StatusDto.valueOf(status), from, size);
    }

}

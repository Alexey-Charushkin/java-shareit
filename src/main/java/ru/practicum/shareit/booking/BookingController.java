package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping(path = "/bookings")
@EnableJpaRepositories
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
                                                   @RequestParam(value = "state", required = false, defaultValue = "ALL") String state) {
        log.info("Get X-Sharer-User-Id, userId");
        return bookingService.getAllBookingsByUserId(userId, state);
    }

    @GetMapping("owner")
    public List<BookingDto> gelAllBookingsByOwnerId(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                    @RequestParam(value = "state", required = false, defaultValue = "ALL") String state) {
        log.info("Get X-Sharer-User-Id, ownerId");
        return bookingService.getAllBookingsByOwnerId(ownerId, state);
    }

}

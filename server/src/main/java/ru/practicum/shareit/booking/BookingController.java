package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemResponseDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping()
    public BookItemResponseDto create(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody BookItemResponseDto bookItemResponseDto) {
        log.info("Post X-Sharer-User-Id");
        return bookingService.create(userId, bookItemResponseDto);
    }

    @PatchMapping("{bookingId}")
    public BookItemResponseDto approveBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId,
                                              @RequestParam("approved") Boolean approved) {
        log.info("Patch X-Sharer-User-Id /:bookindId");
        return bookingService.approveBooking(userId, bookingId, approved);
    }

    @GetMapping("{bookingId}")
    public BookItemResponseDto findByBookingId(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId) {
        log.info("Get X-Sharer-User-Id and bookingId");
        return bookingService.findByBookingId(userId, bookingId);
    }

    @GetMapping()
    public List<BookItemResponseDto> gelAllBookingsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                            @RequestParam(value = "state") String status,
                                                            @RequestParam(name = "from") Integer from,
                                                            @RequestParam(name = "size") Integer size) {
        log.info("Get X-Sharer-User-Id, userId");
        return bookingService.getAllBookingsByUserId(userId, BookItemResponseDto.StatusDto.valueOf(status), from, size);
    }

    @GetMapping("owner")
    public List<BookItemResponseDto> gelAllBookingsByOwnerId(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                             @RequestParam(value = "state") String status,
                                                             @RequestParam(name = "from") Integer from,
                                                             @RequestParam(name = "size") Integer size) {
        log.info("Get X-Sharer-User-Id, ownerId");
        return bookingService.getAllBookingsByOwnerId(ownerId, BookItemResponseDto.StatusDto.valueOf(status), from, size);
    }
}

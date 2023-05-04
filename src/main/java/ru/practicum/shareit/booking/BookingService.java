package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;


import java.util.List;

public interface BookingService {

    BookingDto create(Long userId, BookingDto bookingDto);

    BookingDto findByBookingId(Long userId, Long bookingId);
    BookingDto approveBooking(Long userId, Long bookingId, Boolean approved);
    List<BookingDto> getAllBookingsByUserId(Long userId, String state);
    List<BookingDto> getAllBookingsByOwnerId(Long ownerId, String state);

}

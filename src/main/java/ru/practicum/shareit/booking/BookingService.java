package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;


import java.util.List;

public interface BookingService {

    BookingDto create(Long userId, BookingDto bookingDto);

    //    BookingDto update(Long bookingId, BookingDto bookingDto);
//
//    BookingDto getBookingById(Long bookingId);
    BookingDto findByBookingId(Long userId, Long bookingId);
    BookingDto approveBooking(Long userId, Long bookingId, Boolean approved);
    List<BookingDto> getAllBookingsByUserId(Long userId);
    List<BookingDto> getAllBookingsByOwnerId(Long ownerId);
//
//    void deleteById(Long itemId);
//
//    List<BookingDto> searchBookings(String query);
}

package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;


import java.util.List;

public interface BookingService {

    BookingDto create(Long userId, BookingDto bookingDto);

//    BookingDto update(Long bookingId, BookingDto bookingDto);
//
//    BookingDto getBookingById(Long bookingId);
//
//    List<BookingDto> getAllBookingsByUserId(Long userId);
//
//    void deleteById(Long itemId);
//
//    List<BookingDto> searchBookings(String query);
}

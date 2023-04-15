package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

public class BookingMapper {
    public BookingDto bookingToBookingDto(Booking booking) {
        return new BookingDto(booking);
    }

}

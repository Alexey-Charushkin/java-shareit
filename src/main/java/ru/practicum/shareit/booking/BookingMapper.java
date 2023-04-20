package ru.practicum.shareit.booking;


import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

public class BookingMapper {
    public BookingDto bookingToBookingDto(Booking booking) {
        return new BookingDto();
    }

}

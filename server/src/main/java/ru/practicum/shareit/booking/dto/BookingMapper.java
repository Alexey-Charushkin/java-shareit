package ru.practicum.shareit.booking.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.Booking;

@UtilityClass
public class BookingMapper {

    public BookItemResponseDto toBookingDto(Booking booking) {
        return new BookItemResponseDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem().getId(),
                booking.getItem(),
                booking.getBooker(),
                String.valueOf(booking.getStatus())
        );
    }

    public Booking toBooking(BookItemResponseDto bookItemResponseDto) {
        return new Booking(
                bookItemResponseDto.getId(),
                bookItemResponseDto.getStart(),
                bookItemResponseDto.getEnd(),
                bookItemResponseDto.getItem(),
                bookItemResponseDto.getBooker(),
                String.valueOf(bookItemResponseDto.getStatus())
        );
    }
}

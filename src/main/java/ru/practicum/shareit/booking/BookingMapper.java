package ru.practicum.shareit.booking;


import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemMapper;

public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem().getId(),
                ItemMapper.toItemDto(booking.getItem()),
                booking.getBooker(),
                String.valueOf(booking.getStatus())
        );
    }

    public static Booking toBooking(BookingDto bookingDto) {

        return new Booking(
                bookingDto.getId(),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                ItemMapper.toItem(bookingDto.getBooker(), bookingDto.getItemDto()),
                bookingDto.getBooker(),
                String.valueOf(bookingDto.getStatus())
        );
    }
}

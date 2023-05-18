package ru.practicum.shareit.booking.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item_request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class BookingMapperTest {
    @Autowired
    private JacksonTester<BookingDto> jsonBookingDto;
    @Autowired
    private JacksonTester<Booking> jsonBooking;

    User user = new User(0L, "userName", "email@mail.com");
    User requestor = new User(1L, "requestorName", "requestorEmail@mail.com");
    User booker = new User(2L, "bookerName", "bookerEmil@mail.com");
    ItemRequest request = new ItemRequest(0L, "requestDescription", requestor);
    Item item = new Item(0L, "itemName", "itemDescription",
            true, user, request);

    @SneakyThrows
    @Test
    void toBookingDto() {
        LocalDateTime start = LocalDateTime.now().plusMinutes(1);
        LocalDateTime end = LocalDateTime.now().plusMinutes(5);
        BookingDto bookingDto = new BookingDto(
                1L,
                start,
                end,
                item.getId(),
                item,
                user,
                BookingDto.StatusDto.WAITING.toString());

        JsonContent<BookingDto> result = jsonBookingDto.write(bookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
    }

    @SneakyThrows
    @Test
    void toBooking() {
        LocalDateTime start = LocalDateTime.now().plusMinutes(1);
        LocalDateTime end = LocalDateTime.now().plusMinutes(5);
        Booking booking = new Booking(
                1L,
                start,
                end,
                item, booker, "WAITING");

        JsonContent<Booking> result = jsonBooking.write(booking);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);

    }
}
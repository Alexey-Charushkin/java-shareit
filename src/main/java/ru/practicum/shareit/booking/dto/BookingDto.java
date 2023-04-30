package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Getter
@Setter
public class BookingDto {

    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Item item;

    private User booker;
    private Status status;

    public BookingDto(Long id, LocalDateTime start, LocalDateTime end, Item item, User booker, String status) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.item = item;
        this.booker = booker;
        if (status == null) this.status = Status.WAITING;
        else this.status = Status.valueOf(status);
    }

    private enum Status {WAITING, APPROVED, REJECTED, CANCELED}

}

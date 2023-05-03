package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Getter
@Setter
@NoArgsConstructor

public class BookingDto {

    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Long itemId;

    private Item item;

    private User booker;

    private Status status;

    private State state;


    public BookingDto(Long id, LocalDateTime start, LocalDateTime end, Long itemId, Item item, User booker, String status) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.itemId = itemId;
        this.item = item;
        this.booker = booker;
        if (status == null) this.status = Status.WAITING;
        else this.status = Status.valueOf(status);
    }

    public enum Status {WAITING, APPROVED, REJECTED, CANCELED}
    public enum State {ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED, UNSUPPORTED_STATUS}
}

package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor

public class BookItemResponseDto {

    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
    private Item item;
    private User booker;
    private StatusDto status;

    public BookItemResponseDto(Long id, LocalDateTime start, LocalDateTime end, Long itemId, Item item, User booker,
                               String status) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.itemId = itemId;
        this.item = item;
        this.booker = booker;
        this.status = StatusDto.valueOf(status);

    }

    public enum StatusDto { ALL, APPROVED, CURRENT, PAST, FUTURE, WAITING, REJECTED, CANCELED, UNSUPPORTED_STATUS }

}

package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class LastBooking {
    private Long id;
    private Long bookerId;
}

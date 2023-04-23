package ru.practicum.shareit.request;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Getter
@Setter
public class ItemRequest {

    private Long id;

    private String description;

    private User requestor;

    LocalDateTime created;
}

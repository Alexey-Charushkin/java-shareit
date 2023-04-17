package ru.practicum.shareit.item.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.User;

import java.util.Optional;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
public class Item {

    private Long itemId;

    private String name;

    private String description;
    private Boolean available;

    private User owner;

    private String request;
    public Optional<Boolean> getAvailable() {
        return Optional.ofNullable(available);
    }
}


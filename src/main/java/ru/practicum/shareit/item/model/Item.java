package ru.practicum.shareit.item.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@ToString
public class Item {

    private Long id;

    private String name;

    private String description;
    private boolean available;
    private User owner;

    private String request;

    public Item(User user, Long id, String name, String description, Boolean available) {
        this.owner = user;
        this.id = id;
        this.name = name;
        this.description = description;
       // this.available = available;
        if (available == null) {
            throw new BadRequestException("Available is null.");
        } else {
            this.available = available;
        }
    }
    public Optional<Boolean> getAvailable() {
        return Optional.ofNullable(available);
    }
}


package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
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

    private Long Id;

    private String name;

    private String description;
    private boolean available = false;

    private User owner;

    private String request;

    public Item(User user, Long id, String name, String description, Boolean available) {
    }

    public Boolean getAvailable() {
        return available;
    }
//    public Optional<Boolean> getAvailable() {
//        return Optional.ofNullable(available);
//    }
}


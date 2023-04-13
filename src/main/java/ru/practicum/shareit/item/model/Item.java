package ru.practicum.shareit.item.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {

    private Long itemId;

    private Long ownerId;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    private boolean available;

    public boolean isAvailable() {
        return available;
    }
}


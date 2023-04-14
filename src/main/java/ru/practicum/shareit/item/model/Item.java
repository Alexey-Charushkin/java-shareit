package ru.practicum.shareit.item.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.Optional;

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
    @NotNull
    private Boolean available;

    public Optional<Boolean> getAvailable() {
        return Optional.ofNullable(available);
    }
}


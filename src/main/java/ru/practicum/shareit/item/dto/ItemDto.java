package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@ToString
public class ItemDto {

    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private Boolean available;
    private String request;

    private Integer countRent = 0;

    public Optional<Boolean> getAvailable() {
        return Optional.ofNullable(available);
    }

    public ItemDto(Long id, String name, String description, Boolean available, String request) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.request = request;
    }
}

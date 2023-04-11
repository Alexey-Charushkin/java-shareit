package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
public class ItemDto {

    @NotNull
    @Positive
    private Long itemId;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    private boolean available;

    private Integer countRent;

}

package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.item.model.Item;

import javax.validation.constraints.NotBlank;

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
    @NonNull
    private boolean available;

    private Integer countRent = 0;

    public Boolean getAvailable() {
        return available;
    }

    public ItemDto(Long id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        if (available != null) this.available = available;

    }

//    public ItemDto(Item item, Integer countRent) {
//        this.id = item.getItemId();
//        this.name = item.getName();
//        this.description = item.getDescription();
//        this.available = item.getAvailable().get();
//        this.countRent = countRent;
//    }
}

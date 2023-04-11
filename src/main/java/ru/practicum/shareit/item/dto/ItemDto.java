package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class ItemDto {

    private Long itemId;

    private String name;

    private String description;

    private boolean available;

    private Integer countRent;

    public ItemDto(Item item, Integer countRent) {
        this.itemId = item.getItemId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.available = item.isAvailable();
        this.countRent = countRent;
    }
}

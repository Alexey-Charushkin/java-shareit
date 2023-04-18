package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;

import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
public class ItemDto {

    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;

    private boolean available;

    private Integer countRent;

    public Boolean getAvailable() {
        return available;
    }

    public ItemDto(Long id, String name, String description, Boolean available) {
    }

//    public ItemDto(Item item, Integer countRent) {
//        this.id = item.getItemId();
//        this.name = item.getName();
//        this.description = item.getDescription();
//        this.available = item.getAvailable().get();
//        this.countRent = countRent;
//    }
}

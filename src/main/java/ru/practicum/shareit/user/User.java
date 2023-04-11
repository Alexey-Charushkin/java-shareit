package ru.practicum.shareit.user;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class User {

    private Long userId;

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    private List<Item> items;

}

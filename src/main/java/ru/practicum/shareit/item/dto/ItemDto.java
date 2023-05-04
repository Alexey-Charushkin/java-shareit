package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.LastBooking;
import ru.practicum.shareit.item.model.NextBooking;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ItemDto {

    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private Boolean available;
    private ItemRequest request;

    private Integer countRent = 0;

    private LastBooking lastBooking;

    private NextBooking nextBooking;

    private List<Comment> comments;

    // public Optional<Boolean> getAvailable() {
//        return Optional.ofNullable(available);
//    }
    public Boolean getAvailable() {
        return available;
    }
    public ItemDto(Long id, String name, String description, Boolean available, ItemRequest request) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.request = request;
    }
}

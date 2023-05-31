package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.LastBooking;
import ru.practicum.shareit.booking.dto.NextBooking;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

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
    private Long requestId;

    private Integer countRent = 0;

    private LastBooking lastBooking;

    private NextBooking nextBooking;

    private List<CommentDto> comments;

    public Boolean getAvailable() {
        return available;
    }

    public ItemDto(Long id, String name, String description, Boolean available, ItemRequest request) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        if (request != null) {
            this.requestId = request.getId();
        }
    }
}

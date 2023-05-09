package ru.practicum.shareit.item_request.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ItemRequestDto {

    private Long id;

    private String description;

    private User requestor;

    private LocalDateTime created;

    private List<Item> items;

    public ItemRequestDto(Long id, String description, User requestor, LocalDateTime created, List<Item> items) {
        this.id = id;
        this.description = description;
        this.requestor = requestor;
        this.created = created;
        this.items = items;
    }
}

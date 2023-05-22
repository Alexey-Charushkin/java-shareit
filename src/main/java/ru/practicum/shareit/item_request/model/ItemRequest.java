package ru.practicum.shareit.item_request.model;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "requests", schema = "public")
@Builder
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    private String description;

    @ManyToOne
    @JoinColumn(name = "requestor_id")
    private User requestor;

    @Transient
    private List<ItemDto> items = new ArrayList<>();
    @Column(nullable = false)
    private LocalDateTime created;

    public ItemRequest(String description) {
        this.id = id;
        this.description = description;
        this.created = LocalDateTime.now();
    }


    public ItemRequest(Long id, String description, LocalDateTime created, List<ItemDto> items) {
    }
}

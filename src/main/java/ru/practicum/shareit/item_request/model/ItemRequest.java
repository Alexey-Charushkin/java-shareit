package ru.practicum.shareit.item_request.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "requests", schema = "public")
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "requestor_id")
    private User requestor;

    @ManyToMany
    @JoinColumn(name = "request_id")
    private List<Item> items;
    @Column(nullable = false)
    private LocalDateTime created;

    public ItemRequest(Long id, String description, User requestor) {
        this.id = id;
        this.description = description;
        this.requestor = requestor;
        this.created = LocalDateTime.now();
    }
}

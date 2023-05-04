package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.util.Objects;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "items", schema = "public")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(name = "is_available")
    private boolean available;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    private ItemRequest request;

    public Item(User user, Long id, String name, String description, Boolean available) {
        this.owner = user;
        this.id = id;
        this.name = name;
        this.description = description;
        if (available == null) {
            throw new BadRequestException("Available is null.");
        } else {
            this.available = available;
        }
    }

    public boolean getAvailable() {
        return available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return available == item.available && Objects.equals(id, item.id) && Objects.equals(name, item.name)
                && Objects.equals(description, item.description) && Objects.equals(owner, item.owner)
                && Objects.equals(request, item.request);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, available, owner, request);
    }
}


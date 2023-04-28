package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(name = "is_avaible")
    private boolean available;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

//    @OneToOne
//    @JoinColumn(name = "request_id")
    @Transient
    private String request;

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

    public Optional<Boolean> getAvailable() {
        return Optional.ofNullable(available);
    }
}


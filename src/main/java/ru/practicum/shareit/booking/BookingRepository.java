package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

//    List<Item> findByOwnerId(Long userId);
//
//    @Query("SELECT i FROM Item i " +
//            "WHERE (CAST (i.available AS boolean) = true) " +
//            "AND (UPPER(i.name) LIKE UPPER(CONCAT('%', ?1, '%')) " +
//            "OR UPPER(i.description) LIKE UPPER(CONCAT('%', ?1, '%')))")
//    List<Item> search(String text);
}

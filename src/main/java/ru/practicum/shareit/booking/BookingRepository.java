package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

   // ..  List<Booking> findByBookerIdOrderByDesc(Long userId);
   List<Booking> findByBookerId(Long bookerId, Sort sort);

    @Query("SELECT b FROM Booking b JOIN b.item i JOIN i.owner o WHERE o.id = ?1")
    List<Booking> findByOwnerId(Long ownerId, Sort sort);



    //           (CAST (i.available AS boolean) = true) " +
//            "AND (UPPER(i.name) LIKE UPPER(CONCAT('%', ?1, '%')) " +
//            "OR UPPER(i.description) LIKE UPPER(CONCAT('%', ?1, '%')))")
    //    List<Item> search(String text);

 //   Item findItemByb(Long bookingId);
}

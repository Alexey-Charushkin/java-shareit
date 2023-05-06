package ru.practicum.shareit.booking.dao;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByItemId(Long id, Sort end);

    List<Booking> findByItemIdAndStatus(Long itemId, Booking.Status status, Sort sort);

    List<Booking> findByBookerId(Long bookerId, Sort sort);

    List<Booking> findByBookerIdAndStatus(Long bookerId, Booking.Status status, Sort sort);

    List<Booking> findByBooker_IdAndEndIsBefore(Long bookerId, LocalDateTime end, Sort sort);

    List<Booking> findByBooker_IdAndStartIsAfter(Long bookerId, LocalDateTime start, Sort sort);

    List<Booking> findByBooker_IdAndStartIsBeforeAndEndIsAfter(Long bookerId, LocalDateTime dateTime, LocalDateTime dateTime2, Sort sort);

    @Query("SELECT b FROM Booking b JOIN b.item i JOIN i.owner o WHERE o.id = ?1")
    List<Booking> findByOwnerId(Long ownerId, Sort sort);

    @Query("SELECT b FROM Booking b JOIN b.item i JOIN i.owner o WHERE o.id = ?1 AND b.status = ?2")
    List<Booking> findByOwnerIdAndStatus(Long ownerId, Booking.Status status, Sort sort);

    @Query("SELECT b FROM Booking b JOIN b.item i JOIN i.owner o WHERE o.id = ?1 AND b.end < ?2")
    List<Booking> findByOwnerIdAndEndBefore(Long ownerId, LocalDateTime time, Sort sort);

    @Query("SELECT b FROM Booking b JOIN b.item i JOIN i.owner o WHERE o.id = ?1 AND b.start > ?2")
    List<Booking> findByOwnerIdAndSAndStartIsAfter(Long ownerId, LocalDateTime time, Sort sort);

    @Query("SELECT b FROM Booking b JOIN b.item i JOIN i.owner o WHERE o.id = ?1 AND (b.start < ?2 AND b.end > ?3)")
    List<Booking> findByOwnerIdAndStartIsBeforeAndEndIsAfter(Long ownerId, LocalDateTime time, LocalDateTime time2, Sort sort);
}

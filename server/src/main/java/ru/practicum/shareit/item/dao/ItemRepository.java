package ru.practicum.shareit.item.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwnerId(Long userId, Sort sort);

    List<Item> findByOwnerId(Long userId, Pageable pageable);

    List<Item> findAllByRequestId(Long requestId);

    @Query("SELECT i FROM Item i " +
            "WHERE (CAST (i.available AS boolean) = true) " +
            "AND (UPPER(i.name) LIKE UPPER(CONCAT('%', ?1, '%')) " +
            "OR UPPER(i.description) LIKE UPPER(CONCAT('%', ?1, '%')))")
    List<Item> search(String text);


    @Query("SELECT i FROM Item i " +
            "WHERE (CAST (i.available AS boolean) = true) " +
            "AND (UPPER(i.name) LIKE UPPER(CONCAT('%', ?1, '%')) " +
            "OR UPPER(i.description) LIKE UPPER(CONCAT('%', ?1, '%')))")
    List<Item> searchToPage(String text, Pageable pageable);

    List<Item> findByRequestIdIn(List<Long> requestIds);
}

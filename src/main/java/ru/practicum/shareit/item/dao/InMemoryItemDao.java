package ru.practicum.shareit.item.dao;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.servise.BoyerMoore;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@Log4j2
class InMemoryItemDao implements ItemDao {

    private Long itemId = 0L;
    Map<Long, Item> items = new HashMap<>();

    private final BoyerMoore boyerMoore = new BoyerMoore();

    @Override
    public void add(Item item) {
        item.setId(++itemId);
        log.info("Item create.");
        items.put(item.getId(), item);
    }

    public ItemDto update(Long userId, ItemDto itemDto) {

        Item itemToUpdate = items.get(itemDto.getId());

        if (itemToUpdate == null || !itemToUpdate.getOwner().getId().equals(userId)) {
            log.warn("Item width id={} not found.", itemDto.getId());
            throw new NotFoundException("Item width id=" + itemDto.getId() + " not found.");
        }
        if (itemDto.getName() != null) itemToUpdate.setName(itemDto.getName());
        if (itemDto.getDescription() != null) itemToUpdate.setDescription(itemDto.getDescription());
        if (itemDto.getAvailable().isPresent()) itemToUpdate.setAvailable(itemDto.getAvailable().get());

        items.put(itemId, itemToUpdate);

        log.info("Item updated.");
        return ItemMapper.toItemDto(itemToUpdate);
    }

    @Override
    public List<Item> getAllItemsByUser(Long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .collect(Collectors.toList());
    }

    //
//    @Override
//    public Item remove(Long id) {
//        return items.remove(id);
//    }
//
    @Override
    public Item get(Long id) {
        Item item = items.get(id);
        if (item == null) {
            log.warn("Item with id: {} not found", itemId);
            throw new NotFoundException("Item not found.");
        }
        log.info("Item with id: {} found.", itemId);
        return item;
    }
//
//    @Override
//    public List<Item> search(String query) {
//        List<Item> itemsSearch = new ArrayList<>();
//        if (query.isBlank()) return itemsSearch;
//        for (Item item : getAll().values()) {
//            int i = boyerMoore.search(item.getName().toLowerCase(), query.toLowerCase());
//            int j = boyerMoore.search(item.getDescription().toLowerCase(), query.toLowerCase());
//            if (item.getAvailable().isPresent()) {
//                if (item.getAvailable().get()) {
//                    if (i >= 0 || j >= 0) itemsSearch.add(item);
//                }
//            }
//        }
//        return itemsSearch;
//    }
//
//    @Override
//    public boolean containsKey(Long id) {
//        return items.containsKey(id);
//    }
}

package ru.practicum.shareit.item.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.BoyerMoore;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
class InMemoryItemDao implements ItemDao {

    private BoyerMoore boyerMoore = new BoyerMoore();

    @Override
    public void add(Long id, Item item) {
        items.put(item.getItemId(), item);
    }

    @Override
    public Map<Long, Item> getAll() {
        return items;
    }

    @Override
    public Item remove(Long id) {
        return items.remove(id);
    }

    @Override
    public Item get(Long id) {
        return items.get(id);
    }

    @Override
    public List<Item> search(String query) {
        List<Item> itemsSearch = new ArrayList<>();
        if (query.isBlank()) return itemsSearch;
        for (Item item : getAll().values()) {
            int i = boyerMoore.search(item.getName().toLowerCase(), query.toLowerCase());
            int j = boyerMoore.search(item.getDescription().toLowerCase(), query.toLowerCase());
            if (item.getAvailable().isPresent()) {
                if (item.getAvailable().get()) {
                    if (i >= 0 || j >= 0) itemsSearch.add(item);
                }
            }
        }
        return itemsSearch;
    }

    @Override
    public boolean containsKey(Long id) {
        return items.containsKey(id);
    }
}

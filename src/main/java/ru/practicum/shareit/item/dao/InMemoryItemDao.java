package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.Map;

@Service
class InMemoryItemDao implements ItemDao {

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
    public boolean containsKey(Long id) {
        return items.containsKey(id);
    }
}

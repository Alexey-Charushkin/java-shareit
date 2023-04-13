package ru.practicum.shareit.item;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.UserIsPresentException;
import ru.practicum.shareit.exceptions.notFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
public class ItemServiceImpl implements ItemService {
    private Long itemId = 0L;
    Map<Long, Item> items = new HashMap<>();


    ItemMapper itemMapper = new ItemMapper();

    @Override
    public ItemDto create(Long userId, Item item) {
     //   emailIsPresent(user);
        log.info("Item create.");
        item.setItemId(++itemId);
        item.setOwnerId(userId);
       items.put(item.getItemId(), item);
        return itemMapper.itemToItemDto(item);
    }

    @Override
    public ItemDto update(Long itemId, Item item) {
        Item itemToUpdate = items.get(itemId);
        if (itemToUpdate != null) {
//            if (user.getName() != null) userToUpdate.setName(user.getName());
//            if (user.getEmail() != null && !user.getEmail().equals(userToUpdate.getEmail())) {
//                emailIsPresent(user);
//                userToUpdate.setEmail(user.getEmail());
//            }
            items.put(itemToUpdate.getItemId(), itemToUpdate);
        } else {
            log.warn("Item with id: {} not found", item.getItemId());
            throw new notFoundException("Item not found.");
        }
        log.info("User updated.");
        return itemMapper.itemToItemDto(itemToUpdate);
    }

    @Override
    public ItemDto getById(Long itemId) {
        if (items.containsKey(itemId)) {
            return itemMapper.itemToItemDto(items.get(itemId));
        } else {
            log.warn("Item with id: {} not found", itemId);
            throw new notFoundException("Item not found.");
        }
    }

    @Override
    public List<ItemDto> getAll() {
        ArrayList<ItemDto> itemsDto = new ArrayList<>();
        for (Item item : items.values()) {
            itemsDto.add(itemMapper.itemToItemDto(item));
        }
        return itemsDto;
    }

    @Override
    public ItemDto deleteById(Long itemId) {
        if (items.containsKey(itemId)) {
            log.info("Item with id: {} deleted.", itemId);
            return itemMapper.itemToItemDto(items.get(itemId));
        } else {
            log.warn("Item with id: {} not found.", itemId);
            throw new notFoundException("Item not found.");
        }
    }

//    private void emailIsPresent(User user) {
//        users.values()
//                .stream()
//                .filter(x -> x.getEmail().equals(user.getEmail()))
//                .findFirst()
//                .ifPresent(u -> {
//                    log.warn("User with email: {} is already exists.", user.getEmail());
//                    throw new UserIsPresentException("User with this email is already exists.");
//                });
//    }
}
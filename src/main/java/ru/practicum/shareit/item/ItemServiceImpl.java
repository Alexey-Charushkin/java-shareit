package ru.practicum.shareit.item;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
//@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private Long itemId = 0L;
    private final ItemDao itemDao;

    private UserService userService;

    @Autowired
    public ItemServiceImpl(ItemDao itemDao, UserService userService) {
        this.itemDao = itemDao;
        this.userService = userService;
    }
    private final ItemMapper itemMapper = new ItemMapper();

    @Override
    public ItemDto create(Long userId, Item item) {
        item.setItemId(++itemId);
        item.setOwnerId(userId);
        userService.addItem(userId, item);
        itemDao.add(item.getItemId(), item);
        log.info("Item create.");

        System.out.println(item);

        return itemMapper.itemToItemDto(item);
    }

    @Override
    public ItemDto update(Long itemId, Item item) {

        Item itemToUpdate = itemDao.get(itemId);
        if (itemToUpdate != null) {
//            if (user.getName() != null) userToUpdate.setName(user.getName());
//            if (user.getEmail() != null && !user.getEmail().equals(userToUpdate.getEmail())) {
//                emailIsPresent(user);
//                userToUpdate.setEmail(user.getEmail());
//            }
            itemDao.add(itemToUpdate.getItemId(), itemToUpdate);
        } else {
            log.warn("Item with id: {} not found", item.getItemId());
            throw new NotFoundException("Item not found.");
        }
        log.info("User updated.");
        return itemMapper.itemToItemDto(itemToUpdate);
    }

    @Override
    public ItemDto getById(Long itemId) {
        if (itemDao.containsKey(itemId)) {
            return itemMapper.itemToItemDto(itemDao.get(itemId));
        } else {
            log.warn("Item with id: {} not found", itemId);
            throw new NotFoundException("Item not found.");
        }
    }

    @Override
    public List<ItemDto> getAll() {
        ArrayList<ItemDto> itemsDto = new ArrayList<>();
        Map<Long, Item> items = itemDao.getAll();
        for (Item item : items.values()) {
            itemsDto.add(itemMapper.itemToItemDto(item));
        }
        return itemsDto;
    }

    @Override
    public ItemDto deleteById(Long itemId) {
        if (itemDao.containsKey(itemId)) {
            log.info("Item with id: {} deleted.", itemId);
            return itemMapper.itemToItemDto(itemDao.get(itemId));
        } else {
            log.warn("Item with id: {} not found.", itemId);
            throw new NotFoundException("Item not found.");
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

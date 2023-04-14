package ru.practicum.shareit.item;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.List;

@Log4j2
@Service
public class ItemServiceImpl implements ItemService {
    private Long itemId = 0L;
    private final ItemDao itemDao;

    private final UserService userService;

    @Autowired
    public ItemServiceImpl(ItemDao itemDao, UserService userService) {
        this.itemDao = itemDao;
        this.userService = userService;
    }

    private final ItemMapper itemMapper = new ItemMapper();

    @Override
    public ItemDto create(Long userId, Item item) {

        userService.getById(userId);

        item.setItemId(++itemId);
        item.setOwnerId(userId);
        userService.addItem(userId, item);

        itemDao.add(item.getItemId(), item);

        System.out.println(item);

        log.info("Item create.");

        return itemMapper.itemToItemDto(item);
    }

    @Override
    public ItemDto update(Long userId, Long itemId, Item item) {

        Item itemToUpdate = itemDao.get(itemId);

        if (itemToUpdate != null) {
            if (!itemToUpdate.getOwnerId().equals(userId)) {
                log.warn("This item belongs to another user.");
                throw new NotFoundException("This item belongs to another user.");
            }
            if (item.getName() != null) itemToUpdate.setName(item.getName());
            if (item.getDescription() != null) itemToUpdate.setDescription(item.getDescription());
            if (item.getAvailable().isPresent()) itemToUpdate.setAvailable(item.getAvailable().get());

            userService.addItem(userId, itemToUpdate);

            System.out.println(itemToUpdate);

            itemDao.add(itemId, itemToUpdate);
        } else {
            log.warn("Item with id: {} not found", item.getItemId());
            throw new NotFoundException("Item not found.");
        }
        log.info("Item updated.");
        return itemMapper.itemToItemDto(itemToUpdate);
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        if (itemDao.containsKey(itemId)) {
            log.info("Item with id: {} found.", itemId);
            return itemMapper.itemToItemDto(itemDao.get(itemId));
        } else {
            log.warn("Item with id: {} not found", itemId);
            throw new NotFoundException("Item not found.");
        }
    }

    @Override
    public List<ItemDto> getAllItemsByUser(Long userId) {
        return itemMapper.getItemDtoList(userService.getItemsByIdUser(userId));
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
}

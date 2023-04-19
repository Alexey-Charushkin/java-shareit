package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemDao itemDao;
    private final UserDao userDao;

    @Override
    public ItemDto create(Long userId, ItemDto itemDto) {
        Item item = ItemMapper.toItem(UserMapper.toUser(userDao.get(userId)), itemDto);
        itemDao.add(item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto update(Long userId, ItemDto itemDto) {
        return itemDao.update(userId, itemDto);
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        if (itemId == null) {
            log.warn("Item id id null");
            throw new BadRequestException("Item id is null.");
        }
        return ItemMapper.toItemDto(itemDao.get(itemId));
    }

    @Override
    public List<ItemDto> getAllItemsByUser(Long userId) {
        return itemDao.getAllItemsByUser(userId).stream()
                .map(item -> ItemMapper.toItemDto(item))
                .collect(Collectors.toList());
    }

//    public List<ItemDto> searchItems(String query) {
//        List<ItemDto> items = itemMapper.getItemDtoList(itemDao.search(query));
//        log.info("Request search films, query = {}.", query);
//        return items;
//    }

//    @Override
//    public static ItemDto deleteById(Long itemId) {
//        if (itemDao.containsKey(itemId)) {
//            log.info("Item with id: {} deleted.", itemId);
//            return itemMapper.itemToItemDto(itemDao.get(itemId));
//        } else {
//            log.warn("Item with id: {} not found.", itemId);
//            throw new NotFoundException("Item not found.");
//        }
//    }
}
//
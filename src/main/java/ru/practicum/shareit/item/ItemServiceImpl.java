package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.model.User;

import java.util.List;

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

      //  Item itemToUpdate = itemDao.get(itemDto.getId());

      return itemDao.update(userId, itemDto);

//        if (itemToUpdate != null) {
//            if (!itemToUpdate.getOwner().getId().equals(userId)) {
//                log.warn("This item belongs to another user.");
//                throw new NotFoundException("This item belongs to another user.");
//            }
//            if (itemDto.getName() != null) itemToUpdate.setName(itemDto.getName());
//            if (itemDto.getDescription() != null) itemToUpdate.setDescription(itemDto.getDescription());
//            if (itemDto.getAvailable() != null) itemToUpdate.setAvailable(itemDto.getAvailable());
//
//            userService.addItem(userId, itemToUpdate);
//
//            System.out.println(itemToUpdate);
//
//            itemDao.add(itemId, itemToUpdate);
//        } else {
//            log.warn("Item with id: {} not found", itemDto.getId());
//            throw new NotFoundException("Item not found.");
//        }
//        log.info("Item updated.");
      //  return (itemDto);
    }
//
//    @Override
//    public static ItemDto getItemById(Long itemId) {
//        if (itemDao.containsKey(itemId)) {
//            log.info("Item with id: {} found.", itemId);
//            return itemMapper.itemToItemDto(itemDao.get(itemId));
//        } else {
//            log.warn("Item with id: {} not found", itemId);
//            throw new NotFoundException("Item not found.");
//        }
//    }
//
//    @Override
//    public static List<ItemDto> getAllItemsByUser(Long userId) {
//        return itemMapper.getItemDtoList(userService.getItemsByIdUser(userId));
//    }

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
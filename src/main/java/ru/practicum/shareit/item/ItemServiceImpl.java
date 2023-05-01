package ru.practicum.shareit.item;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;


    @Override
    public ItemDto create(Long userId, ItemDto itemDto) {
        try {
            UserMapper.toUserDto(userRepository.getById(userId));
        } catch (EntityNotFoundException e) {
            log.warn("User with id: {} not found", userId);
            throw new NotFoundException("User not found.");
        }
        Item item = ItemMapper.toItem(userRepository.getById(userId), itemDto);
        itemRepository.save(item);
        log.info("Item create.");
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto update(Long userId, ItemDto itemDto) {
        Item itemToUpdate = itemRepository.getReferenceById(itemDto.getId());
        if (!itemToUpdate.getOwner().getId().equals(userId)) {
            log.warn("The user with id: {} is not the owner of the item", userId);
            throw new NotFoundException("The user is not the owner of the item.");
        }
        if (itemDto.getName() != null) itemToUpdate.setName(itemDto.getName());
        if (itemDto.getDescription() != null) itemToUpdate.setDescription(itemDto.getDescription());
        if (itemDto.getAvailable() != null) itemToUpdate.setAvailable(itemDto.getAvailable());

        itemRepository.save(itemToUpdate);
        log.info("Item updated.");

        System.out.println(itemToUpdate);

        return ItemMapper.toItemDto(itemToUpdate);
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        ItemDto item;
        if (itemId == null) {
            log.warn("Item id id null");
            throw new BadRequestException("Item id is null.");
        }
        try {
            item = ItemMapper.toItemDto(itemRepository.getById(itemId));
        } catch (EntityNotFoundException e) {
            log.warn("Item with id: {} not found", itemId);
            throw new NotFoundException("Item not found.");
        }
        log.info("Item with id: {} found", itemId);
        return (item);
    }

    @Override
    public List<ItemDto> getAllItemsByUserId(Long userId) {
        return itemRepository.findByOwnerId(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }


    public List<ItemDto> searchItems(String query) {
        List<Item> itemList;
        try {
            log.info("Request search films, query = {}.", query);
            itemList = itemRepository.search(query);
        } catch (EntityNotFoundException e) {
            log.warn("Items not found");
            throw new NotFoundException("Item not found.");
        }
        return itemList.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long itemId) {
        itemRepository.deleteById(itemId);
    }
}

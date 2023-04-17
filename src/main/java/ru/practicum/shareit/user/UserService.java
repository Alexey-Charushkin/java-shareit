package ru.practicum.shareit.user;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

public interface UserService {
    UserDto create(UserDto user);

//    UserDto update(Long userid, User user);
//
//    UserDto getById(Long userId);
//
//    List<UserDto> getAll();
//
//    UserDto deleteById(Long userId);
//
//    void addItem(Long userId, Item item);
//
//    List<Item> getItemsByIdUser(Long userId);
}

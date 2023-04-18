package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.DataAlreadyExistException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Override
    public UserDto create(UserDto userDto) {
        return userDao.add(UserMapper.toUser(userDto));
    }

    @Override
    public UserDto update(UserDto userDto) {
        return userDao.update(UserMapper.toUser(userDto));
//        User userToUpdate = userDao.get(userid);
//        if (userToUpdate != null) {
//            if (user.getName() != null) userToUpdate.setName(user.getName());
//            if (user.getEmail() != null && !user.getEmail().equals(userToUpdate.getEmail())) {
//                emailIsPresent(user);
//                userToUpdate.setEmail(user.getEmail());
//            }
//            userDao.add(userToUpdate.getUserId(), userToUpdate);
//        } else {
//            log.warn("User with id: {} not found", user.getUserId());
//            throw new NotFoundException("User not found.");
//        }
//        log.info("User updated.");

    }

    @Override
    public UserDto getById(Long userId) {
        return userDao.get(userId);
    }

    @Override
    public List<UserDto> getAll() {
        return userDao.getAll();
    }

    @Override
    public UserDto deleteById(Long userId) {
        UserDto user = userDao.remove(userId);
        if (user == null) {
            log.warn("User with id: {} not found.", userId);
            throw new NotFoundException("User not found.");
        }
        log.info("User with id: {} deleted.", userId);
        return user;
    }

//    public void addItem(Long userId, Item item) {
//        User user = userDao.get(userId);
//        if (user != null) {
//            Map<Long, Item> userItems = user.getItems();
//            userItems.put(item.getItemId(), item);
//            user.setItems(userItems);
//
//            System.out.println(user);
//
//            userDao.add(user.getUserId(), user);
//        } else {
//            log.warn("User with id: {} not found", userId);
//            throw new NotFoundException("User not found.");
//        }
//    }

//    @Override
//    public List<Item> getItemsByIdUser(Long userId) {
//        User user = userDao.get(userId);
//        return new ArrayList<>(user.getItems().values());
//    }
//
//    private void emailIsPresent(User user) {
//        Map<Long, User> users = userDao.getAll();
//        users.values()
//                .stream()
//                .filter(x -> x.getEmail().equals(user.getEmail()))
//                .findFirst()
//                .ifPresent(u -> {
//                    log.warn("User with email: {} is already exists.", user.getEmail());
//                    throw new DataAlreadyExistException("User with this email is already exists.");
//                });
//    }
}

package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.UserIsPresentException;
import ru.practicum.shareit.exceptions.notFoundException;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private Long userId = 0L;

    private final UserDao userDao;

    private final UserMapper userMapper = new UserMapper();

    @Override
    public UserDto create(User user) {
        emailIsPresent(user);
        log.info("User create.");
        user.setUserId(++userId);
        userDao.add(user.getUserId(), user);
        return userMapper.userToUserDto(user);
    }

    @Override
    public UserDto update(Long userid, User user) {
        User userToUpdate = userDao.get(userid);
        if (userToUpdate != null) {
            if (user.getName() != null) userToUpdate.setName(user.getName());
            if (user.getEmail() != null && !user.getEmail().equals(userToUpdate.getEmail())) {
                emailIsPresent(user);
                userToUpdate.setEmail(user.getEmail());
            }
            userDao.add(userToUpdate.getUserId(), userToUpdate);
        } else {
            log.warn("User with id: {} not found", user.getUserId());
            throw new notFoundException("User not found.");
        }
        log.info("User updated.");
        return userMapper.userToUserDto(userToUpdate);
    }

    @Override
    public UserDto getById(Long userId) {
        if (userDao.containsKey(userId)) {
            return userMapper.userToUserDto(userDao.get(userId));
        } else {
            log.warn("User with id: {} not found", userId);
            throw new notFoundException("User not found.");
        }
    }

    @Override
    public List<UserDto> getAll() {
        ArrayList<UserDto> usersDto = new ArrayList<>();
        Map<Long, User> users = userDao.getAll();

        for (User user : users.values()) {
            usersDto.add(userMapper.userToUserDto(user));
        }
        return usersDto;
    }

    @Override
    public UserDto deleteById(Long userId) {
        if (userDao.containsKey(userId)) {
            log.info("User with id: {} deleted.", userId);
            return userMapper.userToUserDto(userDao.remove(userId));
        } else {
            log.warn("User with id: {} not found.", userId);
            throw new notFoundException("User not found.");
        }
    }

    private void emailIsPresent(User user) {
        Map<Long, User> users = userDao.getAll();
        users.values()
                .stream()
                .filter(x -> x.getEmail().equals(user.getEmail()))
                .findFirst()
                .ifPresent(u -> {
                    log.warn("User with email: {} is already exists.", user.getEmail());
                    throw new UserIsPresentException("User with this email is already exists.");
                });
    }
}

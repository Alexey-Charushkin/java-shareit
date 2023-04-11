package ru.practicum.shareit.user;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.UserIsPresentException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.*;

@Log4j2
@Service
public class UserServiceImpl implements UserService {

    private Long userId = 0L;
    Map<Long, User> users = new HashMap<>();

    UserMapper userMapper = new UserMapper();

    @Override
    public UserDto create(User user) {
        emailIsPresent(user);
        log.info("User create.");
        user.setUserId(++userId);
        users.put(user.getUserId(), user);
        return userMapper.userToUserDto(user);
    }

    @Override
    public UserDto update(Long userid, User user) {
        User userToUpdate = users.get(userid);
        if (userToUpdate != null) {
            if (user.getName() != null) userToUpdate.setName(user.getName());
            if (user.getEmail() != null && !user.getEmail().equals(userToUpdate.getEmail())) {
                emailIsPresent(user);
                userToUpdate.setEmail(user.getEmail());
            }
            users.put(userToUpdate.getUserId(), userToUpdate);
        } else {
            log.warn("User with id: {} not found", user.getUserId());
            throw new UserNotFoundException("User not found.");
        }
        log.info("User updated.");
        return userMapper.userToUserDto(userToUpdate);
    }

    @Override
    public UserDto getById(Long userId) {
        if (users.containsKey(userId)) {
            return userMapper.userToUserDto(users.get(userId));
        } else {
            log.warn("User with id: {} not found", userId);
            throw new UserNotFoundException("User not found.");
        }
    }

    @Override
    public List<UserDto> getAll() {
        ArrayList<UserDto> usersDto = new ArrayList<>();
        for (User user : users.values()) {
            usersDto.add(userMapper.userToUserDto(user));
        }
        return usersDto;
    }

    @Override
    public UserDto deleteById(Long userId) {
        if (users.containsKey(userId)) {
            log.info("User with id: {} deleted.", userId);
            return userMapper.userToUserDto(users.remove(userId));
        } else {
            log.warn("User with id: {} not found.", userId);
            throw new UserNotFoundException("User not found.");
        }
    }

    private void emailIsPresent(User user) {
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

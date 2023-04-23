package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
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
}

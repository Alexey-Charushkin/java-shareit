package ru.practicum.shareit.user.dao;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.DataAlreadyExistException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.*;

@Repository
@Log4j2
public class InMemoryUserDao implements UserDao {

    private Long count = 0L;
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();

    @Override
    public UserDto add(User user) {
        final String email = user.getEmail();
        if (emails.contains(email)) {
            log.warn("Email: {} is already exists.", email);
            throw new DataAlreadyExistException("Email:" + email + " is already exists.");
        }
        user.setId(++count);
        users.put(user.getId(), user);
        emails.add(user.getEmail());
        log.info("User create.");
        return UserMapper.toUserDto(user);
    }

    public UserDto update(User user) {
        final String name = user.getName();
        final String email = user.getEmail();

        User updateUser = users.computeIfPresent(user.getId(), (id, u
                ) -> {
                    if (name != null) u.setName(user.getName());
                    if (email != null) {
                        if (!email.equals(u.getEmail())) {
                            if (emails.contains(email)) {
                                throw new DataAlreadyExistException("Email:" + user.getEmail() + "is already exists.");
                            }
                            emails.remove(u.getEmail());
                            emails.add(email);
                        }
                        u.setEmail(user.getEmail());
                    }
                    return u;
                }
        );
        log.info("User update.");
        return UserMapper.toUserDto(updateUser);
    }

    @Override
    public List<UserDto> getAll() {
        List<User> userList = new ArrayList<>(users.values());
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : userList) {
            userDtos.add(UserMapper.toUserDto(user));
        }
        return userDtos;
    }

    @Override
    public UserDto remove(Long id) {
        if (id == null) return null;
        final User user = users.remove(id);
        emails.remove(user.getEmail());
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto get(Long id) {
        User user = users.getOrDefault(id, null);
        if (user == null) {
            log.warn("User with id: {} not found", id);
            throw new NotFoundException("User not found.");
        }
        log.info("User with id: {} found", id);
        return UserMapper.toUserDto(user);
    }

}

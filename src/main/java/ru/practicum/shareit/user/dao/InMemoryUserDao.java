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
    private Map<Long, User> users = new HashMap<>();
    private Set<String> emails = new HashSet<>();

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

    //    Map<String, String> map = new HashMap<>();
//map.put("name", "Joan");
//
// map.computeIfPresent("name", (key, value) -> key + ", " + value);

    public UserDto update(User user) {
        final String email = user.getEmail();
        final User oldUser = users.get(user.getId());
        if (user.getName() == null || user.getName().isBlank()) user.setName(oldUser.getName());
        if (user.getEmail() == null || user.getEmail().isBlank()) user.setEmail(oldUser.getEmail());

        if (email != null) {
            users.computeIfPresent(user.getId(), (id, u) -> {

                        if (!email.equals(u.getEmail())) {
                            if (emails.contains(email)) {
                                throw new DataAlreadyExistException("Email:" + user.getEmail() + "is already exists.");
                            }
                            emails.remove(u.getEmail());
                            emails.add(email);
                        }
                        return user;
                    }
            );
        }
        users.put(user.getId(), user);
        log.info("User update.");

        System.out.println(user);

        return UserMapper.toUserDto(user);
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

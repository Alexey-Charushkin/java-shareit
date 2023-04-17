package ru.practicum.shareit.user.dao;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.DataAlreadyExistException;
import ru.practicum.shareit.user.User;
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
            throw new DataAlreadyExistException("Email:" + email + "is already exists.");
        }
        user.setId(++count);
        users.put(user.getId(), user);
        emails.add(user.getEmail());
        log.info("User create.");
        return UserMapper.toUserDto(user);
    }

    public void update(User user) {
        final String email = user.getEmail();
        users.computeIfPresent(user.getId(), (id, u) -> {
                    if (!email.equals(u.getEmail())) {
                        if (emails.contains(email)) {
                            throw new DataAlreadyExistException("Email:" + email + "is already exists.");
                        }
                        emails.remove(u.getEmail());
                        emails.add(email);
                    }
                    return user;
                }
        );
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void remove(Long id) {
        final User user = users.remove(id);
        if (user != null) {
            emails.remove(user.getEmail());
        }
    }

    @Override
    public User get(Long id) {
        return users.get(id);
    }

}

package ru.practicum.shareit.user.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTestIT {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    private void addUsers() {
        userRepository.save(User.builder()
                .name("userName")
                .email("userEmail@mail.com")
                .build());
        userRepository.save(User.builder()
                .name("userName2")
                .email("userEmail2@mail.com")
                .build());
    }

    @AfterEach
    private void deleteUsers() {
        userRepository.deleteAll();
    }

    @Test
    void save() {

    }


    @Test
    void findById_whenUserIsPresent_thenReturnUser() {
        Optional<User> actualUser = userRepository.findById(1L);

        assertTrue(actualUser.isPresent());

        assertEquals(actualUser.get().getName(), "userName");
        assertEquals(actualUser.get().getEmail(), "userEmail@mail.com");

    }

    @Test
    void findById_whenUserNotPresent_returnOptionalEmpty() {
        Optional<User> actualUser = userRepository.findById(99L);

        assertTrue(actualUser.isEmpty());
    }


    @Test
    void findAll_whenListUsersIsNotEmpty_thenReturnList() {
        List<User> actualUsers = userRepository.findAll();

        assertEquals(actualUsers.size(), 2);
        assertEquals(actualUsers.get(0).getName(), "userName");
        assertEquals(actualUsers.get(0).getEmail(), "userEmail@mail.com");
        assertEquals(actualUsers.get(1).getName(), "userName2");
        assertEquals(actualUsers.get(1).getEmail(), "userEmail2@mail.com");
    }

    @Test
    void findAll_whenListUsersIsEmpty_thenReturnEmptyList() {
        userRepository.deleteAll();

        List<User> actualUsers = userRepository.findAll();

        assertEquals(actualUsers.size(), 0);
    }

    @Test
    void deleteById_whenUserIsPresent() {
        Optional<User> actualUser = userRepository.findById(1L);

        assertTrue(actualUser.isPresent());

        assertEquals(actualUser.get().getName(), "userName");
        assertEquals(actualUser.get().getEmail(), "userEmail@mail.com");

        userRepository.deleteById(1L);

        actualUser = userRepository.findById(1L);

        assertTrue(actualUser.isEmpty());
    }
}
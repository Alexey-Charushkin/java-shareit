package java.ru.practicum.shareit.user.dao;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql(scripts = {"/test-data.sql"})
class UserRepositoryTestIT {
    @Autowired
    private UserRepository userRepository;

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

        System.out.println(actualUsers.get(0).getId());
        System.out.println(actualUsers.get(1).getId());
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
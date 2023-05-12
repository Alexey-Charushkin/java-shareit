package ru.practicum.shareit.user.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import org.powermock.core.classloader.annotations.PrepareForTest;

import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @PrepareForTest(UserMapper.class)
    @Test
    void create_whenUserDtoIsValid_thenSaveUser() {
        UserDto userToSave = new UserDto(0L, "userName", "email@mail.com");

        User user = UserMapper.toUser(userToSave);
        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        UserDto actualUser = userService.create(userToSave);

        verify(userRepository, times(1)).save(any(User.class));
        assertThat(actualUser.getId()).isEqualTo(userToSave.getId());
        assertThat(actualUser.getName()).isEqualTo(userToSave.getName());
        assertThat(actualUser.getEmail()).isEqualTo(userToSave.getEmail());
    }

    @Test
    void update_whenUserDtoIsValid_thenSaveUser() {
        long userId = 0L;
        UserDto oldUser = new UserDto(userId, "userName", "email@mail.com");
        UserDto updatedUser = new UserDto(userId, "updateName", "updateEmail@mail.com");
        when(userRepository.findById(0L)).thenReturn(Optional.of(UserMapper.toUser(oldUser)));
        when(userRepository.save(any(User.class))).thenReturn(UserMapper.toUser(oldUser));

        UserDto actualUserDto = userService.update(updatedUser);

        verify(userRepository).save(userArgumentCaptor.capture());

        UserDto savedUserDto = UserMapper.toUserDto(userArgumentCaptor.getValue());

        assertEquals(userId, savedUserDto.getId());
        assertEquals("updateName", savedUserDto.getName());
        assertEquals("updateEmail@mail.com", savedUserDto.getEmail());
    }

    @Test
    void getById_whenUserFound_thenReturnUser() {
        long userId = 0L;
        UserDto exceptedUser = new UserDto(0L, "userName", "email@mail.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(UserMapper.toUser(exceptedUser)));

        Optional<UserDto> actualUser = Optional.ofNullable(userService.getById(userId));

        verify(userRepository, times(1)).findById(userId);
        assertThat(exceptedUser.getId()).isEqualTo(actualUser.get().getId());
        assertThat(exceptedUser.getName()).isEqualTo(actualUser.get().getName());
        assertThat(exceptedUser.getEmail()).isEqualTo(actualUser.get().getEmail());
    }

    @Test
    void getById_whenUserNotFound_thenNotFoundExceptionThrown() {
        long userId = 0L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> userService.getById(userId));
        assertEquals(notFoundException.getMessage(), "User not found.");
    }

    @Test
    void getAll_whenUsersFound_thenReturnUsersCollectionInList() {
        User user = new User(0L, "userName", "email@mail.com");
        User user2 = new User(0L, "userName2", "email2@mail.com");
        List<User> exceptedUsers = List.of(user, user2);
        Mockito.when(userRepository.findAll()).thenReturn(exceptedUsers);

        List<User> response = userService.getAll().stream()
                .map(UserMapper::toUser)
                .collect(Collectors.toList());

        assertNotNull(response);
        assertEquals(response.get(0).getId(), exceptedUsers.get(0).getId());
        assertEquals(response.get(0).getName(), exceptedUsers.get(0).getName());
        assertEquals(response.get(0).getEmail(), exceptedUsers.get(0).getEmail());
        assertEquals(response.get(1).getId(), exceptedUsers.get(1).getId());
        assertEquals(response.get(1).getName(), exceptedUsers.get(1).getName());
        assertEquals(response.get(1).getEmail(), exceptedUsers.get(1).getEmail());
    }

    @Test
    void getAll_whenUsersNotFound_thenReturnEmptyList() {
        List<UserDto> response = userService.getAll();

        assertEquals(response, Collections.emptyList());
    }

    @Test
    void deleteById_whenUserFound_thenReturnNotFoundExceptionThrown() {
        long userId = 0L;
        UserDto userToSave = new UserDto(userId, "userName", "email@mail.com");

        User user = UserMapper.toUser(userToSave);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto actualUser = userService.create(userToSave);
        verify(userRepository, times(1)).save(any(User.class));
        assertThat(actualUser.getId()).isEqualTo(userToSave.getId());
        assertThat(actualUser.getName()).isEqualTo(userToSave.getName());
        assertThat(actualUser.getEmail()).isEqualTo(userToSave.getEmail());

        userService.deleteById(userId);

       NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> userService.getById(userId));

        assertEquals(notFoundException.getMessage(), "User not found.");
    }
}
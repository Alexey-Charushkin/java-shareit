package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void create_whenInvoke_thenResponseStatusOkWithUserDtoInBody() {
        long id = 1L;
        UserDto exceptedUserDto = new UserDto();
        exceptedUserDto.setId(id);
        exceptedUserDto.setName("Name");
        exceptedUserDto.setEmail("email@email.com");
        Mockito.when(userService.create(exceptedUserDto)).thenReturn(exceptedUserDto);

        ResponseEntity<UserDto> response = userController.create(exceptedUserDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(exceptedUserDto, response.getBody());
        assertEquals(exceptedUserDto.getId(), response.getBody().getId());
        assertEquals(exceptedUserDto.getName(), response.getBody().getName());
        assertEquals(exceptedUserDto.getEmail(), response.getBody().getEmail());
    }

    @Test
    void update_whenInvoke_thenResponseStatusOkWithUserDtoInBody() {
        long id = 1L;
        UserDto oldExceptedUserDto = new UserDto();
        oldExceptedUserDto.setId(id);
        oldExceptedUserDto.setName("Name");
        oldExceptedUserDto.setEmail("email@email.com");
        userController.create(oldExceptedUserDto);

        UserDto actualExceptedUserDto = new UserDto();
        actualExceptedUserDto.setId(id);
        actualExceptedUserDto.setName("updateName");
        actualExceptedUserDto.setEmail("updateEmail@email.com");
        Mockito.when(userService.update(actualExceptedUserDto)).thenReturn(actualExceptedUserDto);

        ResponseEntity<UserDto> response = userController.update(id, actualExceptedUserDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(actualExceptedUserDto, response.getBody());
        assertEquals(actualExceptedUserDto.getId(), response.getBody().getId());
        assertEquals(actualExceptedUserDto.getName(), response.getBody().getName());
        assertEquals(actualExceptedUserDto.getEmail(), response.getBody().getEmail());
    }

    @Test
    void getById_whenInvoke_thenResponseStatusOkWithUserDtoInBody() {
        long id = 1L;
        UserDto exceptedUserDto = new UserDto();
        exceptedUserDto.setId(id);
        exceptedUserDto.setName("Name");
        exceptedUserDto.setEmail("email@email.com");
        Mockito.when(userService.getById(id)).thenReturn(exceptedUserDto);

        ResponseEntity<UserDto> response = userController.getById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(exceptedUserDto, response.getBody());
        assertEquals(exceptedUserDto.getId(), response.getBody().getId());
        assertEquals(exceptedUserDto.getName(), response.getBody().getName());
        assertEquals(exceptedUserDto.getEmail(), response.getBody().getEmail());
    }

    @Test
    void gelAll_whenInvoke_thenResponseStatusOkWithUsersCollectionInBody() {
        List<UserDto> exceptedUserDtos = List.of(new UserDto());
        Mockito.when(userService.getAll()).thenReturn(exceptedUserDtos);

        ResponseEntity<List<UserDto>> response = userController.gelAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(exceptedUserDtos, response.getBody());
    }

    @Test
    void deleteById_whenInvoke_thenResponseStatusOkWithUserDtoInBodyIsNull() {
        Long id = 1L;
        UserDto exceptedUserDto = new UserDto();
        exceptedUserDto.setId(id);
        exceptedUserDto.setName("Name");
        exceptedUserDto.setEmail("email@email.com");
        Mockito.when(userService.create(exceptedUserDto)).thenReturn(exceptedUserDto);

        ResponseEntity<UserDto> response = userController.create(exceptedUserDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(exceptedUserDto, response.getBody());
        assertEquals(exceptedUserDto.getId(), response.getBody().getId());
        assertEquals(exceptedUserDto.getName(), response.getBody().getName());
        assertEquals(exceptedUserDto.getEmail(), response.getBody().getEmail());

        userController.deleteById(id);

        ResponseEntity<UserDto> currentResponse = userController.getById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(currentResponse.getBody());
    }
}
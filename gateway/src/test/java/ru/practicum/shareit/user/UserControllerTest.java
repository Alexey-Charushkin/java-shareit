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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserClient userClient;
    @InjectMocks
    private UserController userController;

    @Test
    void create_whenInvoke_thenResponseStatusOkWithUserDtoInBody() {
        long id = 1;
        UserDto exceptedUserDto = new UserDto(id, "name", "email@mail.com");
        ResponseEntity<Object> exceptedEntity = new ResponseEntity<>(exceptedUserDto, HttpStatus.OK);
        exceptedUserDto.setId(id);
        exceptedUserDto.setName("Name");
        exceptedUserDto.setEmail("email@email.com");
        Mockito.when(userClient.createUser(exceptedUserDto)).thenReturn(exceptedEntity);

        ResponseEntity<Object> response = userController.create(exceptedUserDto);

        verify(userClient, times(1)).createUser(exceptedUserDto);
        assertEquals(exceptedEntity.getStatusCode(), response.getStatusCode());
        assertEquals(exceptedEntity.getClass(), response.getClass());
        assertEquals(exceptedEntity.getHeaders(), response.getHeaders());
        assertEquals(exceptedEntity.getBody(), response.getBody());
    }

    @Test
    void update_whenInvoke_thenResponseStatusOkWithUserDtoInBody() {
        long id = 1;
        UserDto oldExceptedUserDto = new UserDto(id, "name", "email@mail.com");
        oldExceptedUserDto.setId(id);
        oldExceptedUserDto.setName("Name");
        oldExceptedUserDto.setEmail("email@email.com");
        userController.create(oldExceptedUserDto);

        UserDto actualExceptedUserDto = new UserDto();
        actualExceptedUserDto.setId(id);
        actualExceptedUserDto.setName("updateName");
        actualExceptedUserDto.setEmail("updateEmail@email.com");
        ResponseEntity<Object> exceptedEntity = new ResponseEntity<>(actualExceptedUserDto, HttpStatus.OK);
        Mockito.when(userClient.updateUser(id, actualExceptedUserDto)).thenReturn(exceptedEntity);

        ResponseEntity<Object> response = userController.update(id, actualExceptedUserDto);

        verify(userClient, times(1)).updateUser(id, actualExceptedUserDto);
        assertEquals(exceptedEntity.getStatusCode(), response.getStatusCode());
        assertEquals(exceptedEntity.getClass(), response.getClass());
        assertEquals(exceptedEntity.getHeaders(), response.getHeaders());
        assertEquals(exceptedEntity.getBody(), response.getBody());
    }

    @Test
    void getById_whenInvoke_thenResponseStatusOkWithUserDtoInBody() {
        long id = 1;
        UserDto exceptedUserDto = new UserDto();
        exceptedUserDto.setId(id);
        exceptedUserDto.setName("Name");
        exceptedUserDto.setEmail("email@email.com");
        ResponseEntity<Object> exceptedEntity = new ResponseEntity<>(exceptedUserDto, HttpStatus.OK);
        Mockito.when(userClient.getById(id)).thenReturn(exceptedEntity);

        ResponseEntity<Object> response = userController.getById(id);

        verify(userClient, times(1)).getById(id);
        assertEquals(exceptedEntity.getStatusCode(), response.getStatusCode());
        assertEquals(exceptedEntity.getClass(), response.getClass());
        assertEquals(exceptedEntity.getHeaders(), response.getHeaders());
        assertEquals(exceptedEntity.getBody(), response.getBody());
    }

    @Test
    void gelAll_whenInvoke_thenResponseStatusOkWithUsersCollectionInBody() {
        List<UserDto> exceptedUserDtos = List.of(new UserDto(), new UserDto());
        ResponseEntity<Object> exceptedEntity = new ResponseEntity<>(exceptedUserDtos, HttpStatus.OK);
        Mockito.when(userClient.getAll()).thenReturn(exceptedEntity);

        ResponseEntity<Object> response = userController.gelAll();

        verify(userClient, times(1)).getAll();
        assertEquals(exceptedUserDtos.toString(), response.getBody().toString());
    }

    @Test
    void deleteById_whenInvoke_thenResponseStatusOkWithUserDtoInBodyIsNull() {
        long id = 1;
        UserDto exceptedUserDto = new UserDto();
        exceptedUserDto.setId(id);
        exceptedUserDto.setName("Name");
        exceptedUserDto.setEmail("email@email.com");
        ResponseEntity<Object> exceptedEntity = new ResponseEntity<>(exceptedUserDto, HttpStatus.OK);
        Mockito.when(userClient.createUser(exceptedUserDto)).thenReturn(exceptedEntity);

        ResponseEntity<Object> response = userController.create(exceptedUserDto);

        assertEquals(exceptedEntity.getStatusCode(), response.getStatusCode());
        assertEquals(exceptedEntity.getClass(), response.getClass());
        assertEquals(exceptedEntity.getHeaders(), response.getHeaders());
        assertEquals(exceptedEntity.getBody(), response.getBody());

        userController.deleteById(id);

        ResponseEntity<Object> currentResponse = userController.getById(id);

        assertNull(currentResponse);
    }
}
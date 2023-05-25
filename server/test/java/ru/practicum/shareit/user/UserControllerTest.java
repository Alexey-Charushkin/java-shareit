package java.ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;

    @Test
    void create_whenInvoke_thenResponseStatusOkWithUserDtoInBody() {
        long id = 1L;
        UserDto exceptedUserDto = new UserDto(id, "name", "email@mail.com");
        exceptedUserDto.setId(id);
        exceptedUserDto.setName("Name");
        exceptedUserDto.setEmail("email@email.com");
        Mockito.when(userService.create(exceptedUserDto)).thenReturn(exceptedUserDto);

        UserDto response = userController.create(exceptedUserDto);

        assertEquals(exceptedUserDto.getId(), response.getId());
        assertEquals(exceptedUserDto.getName(), response.getName());
        assertEquals(exceptedUserDto.getEmail(), response.getEmail());
    }

    @Test
    void update_whenInvoke_thenResponseStatusOkWithUserDtoInBody() {
        long id = 1L;
        UserDto oldExceptedUserDto = new UserDto(id, "name", "email@mail.com");
        oldExceptedUserDto.setId(id);
        oldExceptedUserDto.setName("Name");
        oldExceptedUserDto.setEmail("email@email.com");
        userController.create(oldExceptedUserDto);

        UserDto actualExceptedUserDto = new UserDto();
        actualExceptedUserDto.setId(id);
        actualExceptedUserDto.setName("updateName");
        actualExceptedUserDto.setEmail("updateEmail@email.com");
        Mockito.when(userService.update(actualExceptedUserDto)).thenReturn(actualExceptedUserDto);

        UserDto response = userController.update(id, actualExceptedUserDto);

        assertEquals(actualExceptedUserDto.getId(), response.getId());
        assertEquals(actualExceptedUserDto.getName(), response.getName());
        assertEquals(actualExceptedUserDto.getEmail(), response.getEmail());
    }

    @Test
    void getById_whenInvoke_thenResponseStatusOkWithUserDtoInBody() {
        long id = 1L;
        UserDto exceptedUserDto = new UserDto();
        exceptedUserDto.setId(id);
        exceptedUserDto.setName("Name");
        exceptedUserDto.setEmail("email@email.com");
        Mockito.when(userService.getById(id)).thenReturn(exceptedUserDto);

        UserDto response = userController.getById(id);

        assertEquals(exceptedUserDto.getId(), response.getId());
        assertEquals(exceptedUserDto.getName(), response.getName());
        assertEquals(exceptedUserDto.getEmail(), response.getEmail());
    }

    @Test
    void gelAll_whenInvoke_thenResponseStatusOkWithUsersCollectionInBody() {
        List<UserDto> exceptedUserDtos = List.of(new UserDto(), new UserDto());
        Mockito.when(userService.getAll()).thenReturn(exceptedUserDtos);

        List<UserDto> response = userController.gelAll();

        assertEquals(exceptedUserDtos.size(), response.size());
    }

    @Test
    void deleteById_whenInvoke_thenResponseStatusOkWithUserDtoInBodyIsNull() {
        Long id = 1L;
        UserDto exceptedUserDto = new UserDto();
        exceptedUserDto.setId(id);
        exceptedUserDto.setName("Name");
        exceptedUserDto.setEmail("email@email.com");
        Mockito.when(userService.create(exceptedUserDto)).thenReturn(exceptedUserDto);

        UserDto response = userController.create(exceptedUserDto);

        assertEquals(exceptedUserDto.getId(), response.getId());
        assertEquals(exceptedUserDto.getName(), response.getName());
        assertEquals(exceptedUserDto.getEmail(), response.getEmail());

        userController.deleteById(id);

        UserDto currentResponse = userController.getById(id);

        assertNull(currentResponse);
    }
}
package user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTestIT {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @SneakyThrows
    @Test
    void createTest() {
        UserDto userDto = new UserDto(null, "userName", "email@email.com");
        when(userService.create(any(UserDto.class))).thenReturn(userDto);

        String result = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(userService, times(1)).create(any(UserDto.class));
        assertEquals(objectMapper.writeValueAsString(userDto), result);
    }

    @SneakyThrows
    @Test
    void updateTest() {
        Long userId = 1L;
        UserDto userDtoToUpdate = new UserDto(null, "oldUser", "oldUser@mail.com");
        when(userService.update(any(UserDto.class))).thenReturn(userDtoToUpdate);

        String result = mockMvc.perform(patch("/users/{userId}", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDtoToUpdate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(userService, times(1)).update(any(UserDto.class));
        assertEquals(objectMapper.writeValueAsString(userDtoToUpdate), result);

    }

    @SneakyThrows
    @Test
    void getById() {
        Long userId = 1L;
        mockMvc.perform(get("/users/{userId}", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService).getById(userId);
    }

    @SneakyThrows
    @Test
    void gelAll() {
        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService).getAll();

    }

    @SneakyThrows
    @Test
    void deleteById() {
        Long userId = 1L;
        mockMvc.perform(delete("/users/{userId}", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService).deleteById(userId);
    }
}

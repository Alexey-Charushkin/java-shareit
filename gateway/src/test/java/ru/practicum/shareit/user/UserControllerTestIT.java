package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;

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
    private UserClient userClient;

    @SneakyThrows
    @Test
    void createTest() {
        UserDto userDto = new UserDto(null, "userName", "email@email.com");
        ResponseEntity<Object> exceptedEntity = new ResponseEntity<>(userDto, HttpStatus.OK);
        when(userClient.createUser(any(UserDto.class))).thenReturn(exceptedEntity);

        String result = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(userClient, times(1)).createUser(any(UserDto.class));
        assertEquals(objectMapper.writeValueAsString(userDto), result);
    }

    @SneakyThrows
    @Test
    void updateTest() {
        Long userId = 1L;
        UserDto userDtoToUpdate = new UserDto(null, "oldUser", "oldUser@mail.com");
        ResponseEntity<Object> exceptedEntity = new ResponseEntity<>(userDtoToUpdate, HttpStatus.OK);
        when(userClient.updateUser(anyLong(), any(UserDto.class))).thenReturn(exceptedEntity);

        String result = mockMvc.perform(patch("/users/{userId}", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDtoToUpdate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(userClient, times(1)).updateUser(anyLong(), any(UserDto.class));
        assertEquals(objectMapper.writeValueAsString(userDtoToUpdate), result);
    }

    @SneakyThrows
    @Test
    void getById() {
        long userId = 1;
        mockMvc.perform(get("/users/{userId}", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userClient).getById(userId);
    }

    @SneakyThrows
    @Test
    void gelAll() {
        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userClient).getAll();

    }

    @SneakyThrows
    @Test
    void deleteById() {
        long userId = 1;
        mockMvc.perform(delete("/users/{userId}", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userClient).deleteById(userId);
    }
}

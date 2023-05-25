package java.ru.practicum.shareit.user.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class UserMapperTest {
    @Autowired
    private JacksonTester<UserDto> jsonUserDto;
    @Autowired
    private JacksonTester<User> jsonUser;

    @SneakyThrows
    @Test
    void toUserDto_whenUserDtoIsCorrect_thenReturnUserDto() {
        UserDto userDto = new UserDto(
                1L,
                "userDtoName",
                "userDtoEmail@mail.com");

        JsonContent<UserDto> result = jsonUserDto.write(userDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("userDtoName");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("userDtoEmail@mail.com");
    }

    @SneakyThrows
    @Test
    void toUser() {
        User user = new User(1L,
                "userName",
                "email@mail.com");

        JsonContent<User> result = jsonUser.write(user);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("userName");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("email@mail.com");
    }
}


package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

public class UserMapper {

    public UserDto userToUserDto(User user) {
        return new UserDto(user);
    }

}

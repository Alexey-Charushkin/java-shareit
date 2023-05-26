package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping()
    public UserDto create(@Validated(Create.class) @RequestBody UserDto userDto) {
        log.info("Post /users");
        return userService.create(userDto);
    }

    @PatchMapping("{userId}")
    public UserDto update(@PathVariable Long userId, @Validated(Update.class) @RequestBody UserDto userDto) {
        log.info("Patch /users");
        userDto.setId(userId);
        return userService.update(userDto);
    }

    @GetMapping("{userId}")
    public UserDto getById(@PathVariable Long userId) {
        log.info("Get /users/{userId}");
        return userService.getById(userId);
    }

    @GetMapping()
    public List<UserDto> gelAll() {
        log.info("Get /users");
        return userService.getAll();
    }

    @DeleteMapping("{userId}")
    public void deleteById(@PathVariable Long userId) {
        log.info("Delete /users/{userId}");
        userService.deleteById(userId);
    }

}

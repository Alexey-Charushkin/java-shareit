package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping()
    public UserDto create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PatchMapping("{userId}")
    public UserDto update(@PathVariable Long userId, @RequestBody User user) {
        return userService.update(userId, user);
    }

    @GetMapping("{userId}")
    public UserDto getById(@PathVariable Long userId) {
        return userService.getById(userId);
    }

    @GetMapping()
    public List<UserDto> gelAll() {
        return userService.getAll();
    }

    @DeleteMapping("{userId}")
    public UserDto deleteById(@PathVariable Long userId) {
        return userService.deleteById(userId);
    }

}

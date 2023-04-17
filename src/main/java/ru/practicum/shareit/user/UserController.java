package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping()
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        log.info("Post user.");
        return userService.create(userDto);
    }

//    @PatchMapping("{userId}")
//    public UserDto update(@PathVariable Long userId, @Valid @RequestBody UserDto user) {
//        log.info("Patch user.");
//        user.setId(userId);
//        return userService.update(user);
//    }
//
//    @GetMapping("{userId}")
//    public UserDto getById(@PathVariable Long userId) {
//        log.info("Get user.");
//        return userService.getById(userId);
//    }
//
//    @GetMapping()
//    public List<UserDto> gelAll() {
//        log.info("Get all users.");
//        return userService.getAll();
//    }

//    @DeleteMapping("{userId}")
//    public UserDto deleteById(@PathVariable Long userId) {
//        log.info("Delete user by Id.");
//        return userService.deleteById(userId);
//    }

}

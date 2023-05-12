package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping()
    public ResponseEntity<UserDto> create(@Valid @RequestBody UserDto userDto) {
        log.info("Post /users");
        return ResponseEntity.ok(userService.create(userDto));
    }

    @PatchMapping("{userId}")
    public ResponseEntity<UserDto> update(@PathVariable Long userId, @RequestBody UserDto userDto) {
        log.info("Patch /users");
        userDto.setId(userId);
        return ResponseEntity.ok(userService.update(userDto));
    }

    @GetMapping("{userId}")
    public ResponseEntity<UserDto> getById(@PathVariable Long userId) {
        log.info("Get /users/{userId}");
        return ResponseEntity.ok(userService.getById(userId));
    }

    @GetMapping()
    public ResponseEntity<List<UserDto>> gelAll() {
        log.info("Get /users");
        return ResponseEntity.ok(userService.getAll());
    }

    @DeleteMapping("{userId}")
    public void deleteById(@PathVariable Long userId) {
        log.info("Delete /users/{userId}");
        userService.deleteById(userId);
    }

}

package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

@Slf4j
@Log4j2
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> create(@Validated(Create.class) @RequestBody UserDto userDto) {
        log.info("Creating user {}", userDto);
        return userClient.createUser(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@PathVariable Long userId, @Validated(Update.class) @RequestBody UserDto userDto) {
        log.info("Updating user {}", userDto);
        return userClient.updateUser(userId, userDto);
    }

    @GetMapping("{userId}")
    public ResponseEntity<Object> getById(@PathVariable Long userId) {
        log.info("Get user by id: {}", userId);
        return userClient.getById(userId);
    }

    @GetMapping()
    public ResponseEntity<Object> gelAll() {
        log.info("Get all users");
        return userClient.getAll();
    }

    @DeleteMapping("{userId}")
    public void deleteById(@PathVariable Long userId) {
        log.info("Delete user by id: {}", userId);
        userClient.deleteById(userId);
    }
}

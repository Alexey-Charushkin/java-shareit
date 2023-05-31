package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDtoToSave;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
@Log4j2
public class ItemRequestController {

    private final RequestClient requestClient;

    @PostMapping()
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemRequestDtoToSave itemRequestDtoToSave) {
        log.info("Posts/requests");
        return requestClient.create(userId, itemRequestDtoToSave);
    }

    @GetMapping()
    public ResponseEntity<Object> findAllByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get/requests");
        return requestClient.findAllByUserId(userId);
    }

    @GetMapping("{requestId}")
    public ResponseEntity<Object> findById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long requestId) {
        log.info("Get/requests/requestId");
        return requestClient.findById(userId, requestId);
    }

    @GetMapping("all")
    public ResponseEntity<Object> findAllById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                    @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                    @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get/requests/all?from={from}&size={size}");
        return requestClient.findAllByUserIdToPageable(userId, from, size);
    }
}

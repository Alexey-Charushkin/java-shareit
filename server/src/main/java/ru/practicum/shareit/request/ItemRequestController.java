package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDtoToReturn;
import ru.practicum.shareit.request.dto.ItemRequestDtoToSave;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
@Log4j2
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping()
    public ItemRequestDtoToReturn create(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemRequestDtoToSave itemRequestDtoToSave) {
        log.info("Posts/requests");
        return itemRequestService.create(userId, itemRequestDtoToSave);
    }

    @GetMapping()
    public List<ItemRequestDtoToReturn> findAllByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get/requests");
        return itemRequestService.findAllByUserId(userId);
    }

    @GetMapping("{requestId}")
    public ItemRequestDtoToReturn findById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long requestId) {
        log.info("Get/requests/requestId");
        return ItemRequestMapper.toItemRequestDto(itemRequestService.findById(userId, requestId));
    }

    @GetMapping("all")
    public List<ItemRequestDtoToReturn> findAllById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                    @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                    @Positive @RequestParam(name = "size", required = false) Integer size) {
        log.info("Get/requests/all?from={from}&size={size}");
        return itemRequestService.findAllByUserIdToPageable(userId, from, size);
    }
}

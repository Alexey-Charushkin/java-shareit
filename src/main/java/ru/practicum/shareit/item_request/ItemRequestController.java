package ru.practicum.shareit.item_request;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item_request.dto.ItemRequestDto;
import ru.practicum.shareit.item_request.service.ItemRequestService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Log4j2
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping()
    public ItemRequestDto create(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Posts/requests");
        return itemRequestService.create(userId, itemRequestDto);
    }

    @GetMapping()
    public List<ItemRequestDto> findAllByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get/requests");
        return itemRequestService.findAllByUserId(userId);
    }
    @GetMapping({"/requestId"})
    public ItemRequestDto findById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long requestId) {
        log.info("Get/requests/requestId");
        return itemRequestService.findById(userId, requestId);
    }
}

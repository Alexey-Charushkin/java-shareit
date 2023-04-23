package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping()
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemDto itemDto) {
        log.info("Post X-Sharer-User-Id");
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @PathVariable Long itemId, @RequestBody ItemDto itemDto) {
        log.info("Patch X-Sharer-User-Id /{itemId}");
        itemDto.setId(itemId);
        return itemService.update(userId, itemDto);
    }

    @GetMapping("{itemId}")
    public ItemDto getById(@PathVariable Long itemId) {
        log.info("Get /{itemId}");
        return itemService.getItemById(itemId);
    }

    @GetMapping()
    public List<ItemDto> gelAllByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get X-Sharer-User-Id");
        return itemService.getAllItemsByUser(userId);
    }

    @DeleteMapping("{itemId}")
    public ItemDto deleteById(@PathVariable Long itemId) {
        log.info("Delete /{itemId}");
        return itemService.deleteById(itemId);
    }

    @GetMapping("search")
    public List<ItemDto> searchItems(@RequestParam("text") String query) {
        log.info("Get =search");
        if (query == null || query.isBlank()) return Collections.emptyList();
        return itemService.searchItems(query);
    }

}

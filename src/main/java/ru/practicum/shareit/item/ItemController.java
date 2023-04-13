package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping()
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long userId,  @Valid @RequestBody Item item) {
        return itemService.create(userId, item);
    }

    @PatchMapping("{itemId}")
    public ItemDto update(@PathVariable Long itemId, @RequestBody Item item) {
        return itemService.update(itemId, item);
    }

    @GetMapping("{itemId}")
    public ItemDto getById(@PathVariable Long itemId) {
        return itemService.getById(itemId);
    }

    @GetMapping()
    public List<ItemDto> gelAll() {
        return itemService.getAll();
    }

    @DeleteMapping("{itemId}")
    public ItemDto deleteById(@PathVariable Long itemId) {
        return itemService.deleteById(itemId);
    }

}

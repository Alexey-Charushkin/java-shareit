package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

//    private final ItemService itemService;
//
//    @PostMapping()
//    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long userId,  @Valid @RequestBody ItemDto item) {
//        return itemService.create(userId, item);
//    }
//
//    @PatchMapping("{itemId}")
//    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @PathVariable Long itemId, @RequestBody ItemDto item) {
//        return itemService.update(userId, itemId, item);
//    }
//
//    @GetMapping("{itemId}")
//    public ItemDto getById(@PathVariable Long itemId) {
//        return itemService.getItemById(itemId);
//    }
//
//    @GetMapping()
//    public List<ItemDto> gelAllByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
//        return itemService.getAllItemsByUser(userId);
//    }
//
//    @DeleteMapping("{itemId}")
//    public ItemDto deleteById(@PathVariable Long itemId) {
//        return itemService.deleteById(itemId);
//    }
//
//    @GetMapping("search")
//    public List<ItemDto> searchItems(@RequestParam("text") String query) {
//        return itemService.searchItems(query);
//    }

}

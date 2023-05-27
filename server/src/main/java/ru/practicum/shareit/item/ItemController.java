package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.service.CommentService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    private final CommentService commentService;

    @PostMapping()
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody ItemDto itemDto) {
        log.info("Post X-Sharer-User-Id");
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId,
                          @RequestBody ItemDto itemDto) {
        log.info("Patch X-Sharer-User-Id /{itemId}");
        itemDto.setId(itemId);
        return itemService.update(userId, itemDto);
    }

    @GetMapping("{itemId}")
    public ItemDto getById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        log.info("Get /{itemId}");
        return itemService.getItemById(userId, itemId);
    }

    @GetMapping()
    public List<ItemDto> gelAllByUserId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @RequestParam(name = "from") Integer from,
                                        @RequestParam(name = "size") Integer size) {
        log.info("Get X-Sharer-User-Id");
        return itemService.getAllItemsByUserId(userId, from, size);
    }

    @DeleteMapping("{itemId}")
    public void deleteById(@PathVariable Long itemId) {
        log.info("Delete /{itemId}");
        itemService.deleteById(itemId);
    }

    @GetMapping("search")
    public List<ItemDto> searchItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @RequestParam("text") String query,
                                     @RequestParam(name = "from") Integer from,
                                     @RequestParam(name = "size") Integer size) {
        log.info("Get =search");
        return itemService.searchItems(query, from, size);
    }

    @PostMapping("{itemId}/comment")
    public CommentDto create(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId,
                             @RequestBody CommentDto commentDto) {
        log.info("Post X-Sharer-User-Id {itemId}/comment");
        return commentService.create(userId, itemId, commentDto);
    }

}

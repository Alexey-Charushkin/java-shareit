package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RestController
@RequiredArgsConstructor
@Log4j2
@Validated
@RequestMapping("/items")
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping()
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemDto itemDto) {
        log.info("Post X-Sharer-User-Id");
        return itemClient.create(userId, itemDto);
    }

    @PatchMapping("{itemId}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @PathVariable Long itemId, @RequestBody ItemDto itemDto) {
        log.info("Patch X-Sharer-User-Id /{itemId}");
        itemDto.setId(itemId);
        return itemClient.update(userId, itemId, itemDto);
    }

    @GetMapping("{itemId}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        log.info("Get /{itemId}");
        return itemClient.getItemById(userId, itemId);
    }

    @GetMapping()
    public ResponseEntity<Object> gelAllByUserId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                        @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get X-Sharer-User-Id");
        return itemClient.getAllItemsByUserId(userId, from, size);
    }

    @DeleteMapping("{itemId}")
    public void deleteById(@PathVariable Long itemId) {
        log.info("Delete /{itemId}");
        itemClient.deleteById(itemId);
    }

    @GetMapping("search")
    public ResponseEntity<Object> searchItems(@RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam("text") String query,
                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                     @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get =search");
        return itemClient.searchItems(userId, query, from, size);
    }

    @PostMapping("{itemId}/comment")
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId,
                             @Valid @RequestBody CommentDto commentDto) {
        log.info("Post X-Sharer-User-Id {itemId}/comment");
        return itemClient.createComment(userId, itemId, commentDto);
    }
}

package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ItemInfoDto get(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable int itemId) {
        log.info("==> get itemId = {}, ownerId = {}", itemId, userId);
        ItemInfoDto item = itemService.get(itemId);
        log.info("<== get item: {}, ownerId = {}", item, userId);
        return item;
    }

    @GetMapping
    public List<ItemInfoDto> findByOwnerId(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("==> findByOwnerId ownerId = {}", userId);
        List<ItemInfoDto> items = itemService.findByOwnerId(userId);
        log.info("<== findByOwnerId {} ownerId = {}", items.size(), userId);
        return items;
    }

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") long userId, @Valid @RequestBody ItemCreateDto newItem) {
        log.info("==> create item: {}, ownerId = {}", newItem, userId);
        ItemDto item = itemService.add(newItem, userId);
        log.info("<== create item: {}, ownerId = {}", item, userId);
        return item;
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId, @Valid @RequestBody ItemDto newItem) {
        log.info("==> update item: {}, ownerId = {}", newItem, userId);
        newItem.setId(itemId);
        ItemDto item = itemService.update(newItem, userId);
        log.info("<== update item: {}, ownerId = {}", item, userId);
        return item;
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam String text) {
        log.info("==> search by: {}, ownerId = {}", text, userId);
        List<ItemDto> items = itemService.search(text, userId);
        log.info("<== search by: {} - {}, ownerId = {}", text, items.size(), userId);
        return items;
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId, @RequestBody CommentCreateDto comment) {
        log.info("==> add comment: {}, authorId = {}, itemId = {}", comment, userId, itemId);
        CommentDto commentDto = itemService.addComment(userId, itemId, comment);
        log.info("<== add comment: {}", commentDto);
        return commentDto;
    }
}

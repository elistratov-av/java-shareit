package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.util.Headers;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> get(@RequestHeader(Headers.USER_ID) long userId, @PathVariable long itemId) {
        log.info("==> get itemId = {}, ownerId = {}", itemId, userId);
        ResponseEntity<Object> item = itemClient.get(userId, itemId);
        return item;
    }

    @GetMapping
    public ResponseEntity<Object> findByOwnerId(@RequestHeader(Headers.USER_ID) long userId) {
        log.info("==> findByOwnerId ownerId = {}", userId);
        ResponseEntity<Object> items = itemClient.findByOwnerId(userId);
        return items;
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(Headers.USER_ID) long userId, @RequestBody @Valid ItemCreateDto newItem) {
        log.info("==> create item: {}, ownerId = {}", newItem, userId);
        ResponseEntity<Object> item = itemClient.add(userId, newItem);
        return item;
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader(Headers.USER_ID) long userId, @PathVariable long itemId, @RequestBody @Valid ItemDto newItem) {
        log.info("==> update item: {}, ownerId = {}", newItem, userId);
        newItem.setId(itemId);
        ResponseEntity<Object> item = itemClient.update(userId, newItem);
        return item;
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestHeader(Headers.USER_ID) long userId, @RequestParam String text) {
        log.info("==> search by: {}, ownerId = {}", text, userId);
        ResponseEntity<Object> items = itemClient.search(userId, text);
        return items;
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(Headers.USER_ID) long userId, @PathVariable long itemId, @RequestBody @Valid CommentCreateDto newComment) {
        log.info("==> add newComment: {}, authorId = {}, itemId = {}", newComment, userId, itemId);
        ResponseEntity<Object> comment = itemClient.addComment(userId, itemId, newComment);
        return comment;
    }
}

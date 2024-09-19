package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ItemDto get(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable int itemId) {
        ItemDto item = itemService.get(itemId);
        log.info("Получена вещь: {}, для владельца с id = {}", item, userId);
        return item;
    }

    @GetMapping
    public List<ItemDto> findAll(@RequestHeader("X-Sharer-User-Id") long userId) {
        List<ItemDto> items = itemService.findAll(userId);
        log.info("Получен список вещей для владельца с id = {}", userId);
        return items;
    }

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") long userId, @Valid @RequestBody ItemDto item) {
        item = itemService.add(item, userId);
        log.info("Создана вещь: {}, для владельца с id = {}", item, userId);
        return item;
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId, @Valid @RequestBody ItemDto newItem) {
        newItem.setId(itemId);
        ItemDto item = itemService.update(newItem, userId);
        log.info("Изменена вещь: {}, для владельца с id = {}", item, userId);
        return item;
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam String text) {
        List<ItemDto> items = itemService.search(text, userId);
        log.info("Поиск вещей по тексту: {}, для владельца с id = {}", text, userId);
        return items;
    }
}

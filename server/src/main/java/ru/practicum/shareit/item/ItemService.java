package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;

import java.util.List;

public interface ItemService {
    ItemInfoDto get(long itemId);

    List<ItemInfoDto> findByOwnerId(long userId);

    ItemDto add(ItemDto item, long userId);

    ItemDto update(ItemDto newItem, long userId);

    List<ItemDto> search(String text);

    CommentDto addComment(long userId, long itemId, CommentCreateDto newComment);
}

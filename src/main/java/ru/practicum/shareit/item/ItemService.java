package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto get(long itemId);

    List<ItemDto> findAll(long userId);

    ItemDto add(ItemCreateDto item, long userId);

    ItemDto update(ItemDto newItem, long userId);

    List<ItemDto> search(String text, long userId);
}

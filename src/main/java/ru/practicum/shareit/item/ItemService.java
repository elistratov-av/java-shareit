package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {
    ItemDto get(long itemId);

    List<ItemDto> findAll(long userId);

    ItemDto add(ItemDto item, long userId);

    ItemDto update(ItemDto newItem, long userId);

    List<ItemDto> search(String text, long userId);
}

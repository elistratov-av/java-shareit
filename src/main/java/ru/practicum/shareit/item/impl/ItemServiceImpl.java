package ru.practicum.shareit.item.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto get(long id) {
        Item item = itemRepository.get(id)
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + id + " не найдена"));
        return itemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> findAll(long userId) {
        userRepository.get(userId)
                .orElseThrow(() -> new NotFoundException("Владелец с id = " + userId + " не найден"));
        return itemMapper.toItemDtoList(itemRepository.findAll(userId));
    }

    @Override
    public ItemDto add(ItemCreateDto newItem, long userId) {
        User owner = userRepository.get(userId)
                .orElseThrow(() -> new NotFoundException("Владелец с id = " + userId + " не найден"));
        Item item = itemRepository.add(itemMapper.toItem(newItem, owner, null));
        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemDto update(ItemDto newItem, long userId) {
        User owner = userRepository.get(userId)
                .orElseThrow(() -> new NotFoundException("Владелец с id = " + userId + " не найден"));
        Item item = itemRepository.update(itemMapper.toItem(newItem, owner, null));
        return itemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> search(String text, long userId) {
        userRepository.get(userId)
                .orElseThrow(() -> new NotFoundException("Владелец с id = " + userId + " не найден"));
        if (StringUtils.isBlank(text))
            return new ArrayList<>();
        return itemMapper.toItemDtoList(itemRepository.search(text, userId));
    }
}

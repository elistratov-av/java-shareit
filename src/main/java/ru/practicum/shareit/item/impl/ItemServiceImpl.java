package ru.practicum.shareit.item.impl;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto get(long id) {
        Item item = itemRepository.get(id)
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + id + " не найдена"));
        return ItemMapper.INSTANCE.toItemDto(item);
    }

    @Override
    public List<ItemDto> findAll(long userId) {
        userRepository.get(userId)
                .orElseThrow(() -> new NotFoundException("Владелец с id = " + userId + " не найден"));
        return ItemMapper.INSTANCE.toItemDtoList(itemRepository.findAll(userId));
    }

    @Override
    public ItemDto add(ItemDto newItem, long userId) {
        User owner = userRepository.get(userId)
                .orElseThrow(() -> new NotFoundException("Владелец с id = " + userId + " не найден"));
        if (StringUtils.isBlank(newItem.getName()))
            throw new ValidationException("Краткое название вещи не заполнено");
        if (StringUtils.isBlank(newItem.getDescription()))
            throw new ValidationException("Развёрнутое описание вещи не заполнено");
        if (newItem.getAvailable() == null)
            throw new ValidationException("Статус доступности для аренды вещи не заполнен");
        Item item = itemRepository.add(ItemMapper.INSTANCE.toItem(newItem, owner, null));
        return ItemMapper.INSTANCE.toItemDto(item);
    }

    @Override
    public ItemDto update(ItemDto newItem, long userId) {
        User owner = userRepository.get(userId)
                .orElseThrow(() -> new NotFoundException("Владелец с id = " + userId + " не найден"));
        Item item = itemRepository.update(ItemMapper.INSTANCE.toItem(newItem, owner, null));
        return ItemMapper.INSTANCE.toItemDto(item);
    }

    @Override
    public List<ItemDto> search(String text, long userId) {
        userRepository.get(userId)
                .orElseThrow(() -> new NotFoundException("Владелец с id = " + userId + " не найден"));
        if (StringUtils.isBlank(text))
            return new ArrayList<>();
        return ItemMapper.INSTANCE.toItemDtoList(itemRepository.search(text, userId));
    }
}

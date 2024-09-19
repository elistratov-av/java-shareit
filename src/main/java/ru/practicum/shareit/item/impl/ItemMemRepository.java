package ru.practicum.shareit.item.impl;

import jakarta.validation.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ItemMemRepository implements ItemRepository {
    private long idCounter = 0L;
    private final Map<Long, Item> items = new HashMap<>();

    protected long nextId() {
        return ++idCounter;
    }

    @Override
    public Optional<Item> get(long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public List<Item> findAll(long ownerId) {
        return items.values().stream()
                .filter(i -> i.getOwner() != null && i.getOwner().getId() == ownerId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> search(String text, long ownerId) {
        String t = text.toUpperCase();
        return items.values().stream()
                .filter(i -> i.getAvailable()
                        && ((i.getName() != null && i.getName().toUpperCase().contains(t))
                        || (i.getDescription() != null && i.getDescription().toUpperCase().contains(t))))
                .collect(Collectors.toList());
    }

    @Override
    public Item add(Item newItem) {
        // формируем дополнительные данные
        newItem.setId(nextId());
        // сохраняем новую вещь в памяти приложения
        items.put(newItem.getId(), newItem);
        return newItem;
    }

    @Override
    public Item update(Item newItem) {
        // проверяем необходимые условия
        Item oldItem = get(newItem.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + newItem.getId() + " не найден"));
        if (!newItem.getOwner().equals(oldItem.getOwner()))
            throw new ValidationException("Редактировать разрешено только владельцу");

        // если вещь найдена и все условия соблюдены, обновляем ее содержимое
        if (StringUtils.isNoneBlank(newItem.getName())) oldItem.setName(newItem.getName());
        if (StringUtils.isNoneBlank(newItem.getDescription())) oldItem.setDescription(newItem.getDescription());
        if (newItem.getAvailable() != null) oldItem.setAvailable(newItem.getAvailable());
        return oldItem;
    }

    @Override
    public void deleteByOwnerId(long ownerId) {
        items.values().removeIf(i -> i.getOwner() != null && i.getOwner().getId() == ownerId);
    }
}

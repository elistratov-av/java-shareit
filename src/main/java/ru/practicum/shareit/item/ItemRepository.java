package ru.practicum.shareit.item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Optional<Item> get(long id);

    List<Item> findAll(long ownerId);

    List<Item> search(String text, long ownerId);

    Item add(Item newItem);

    Item update(Item newItem);

    void deleteByOwnerId(long ownerId);
}

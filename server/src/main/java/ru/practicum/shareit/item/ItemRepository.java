package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwnerId(long ownerId);

    List<Item> findByRequestIdIn(List<Long> requestIds);

    List<Item> findByRequestId(long requestId);

    @Query("select i " +
            "from Item i " +
            "where i.available " +
            "and (UPPER(i.name) like UPPER(%?1%) or UPPER(i.description) like UPPER(%?1%))")
    List<Item> searchByNameOrDescription(String text);
}

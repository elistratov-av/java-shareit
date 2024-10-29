package ru.practicum.shareit.item;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByItemIdIn(List<Long> itemIds, Sort sort);

    List<Comment> findByItemId(long itemId, Sort sort);
}

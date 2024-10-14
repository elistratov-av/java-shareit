package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
public class CommentDto {
    private Long id; // уникальный идентификатор комментария
    private String text; // текст комментария
    private String authorName; // пользователь, написавший комментарий
    private LocalDateTime created; // дата создания комментария
}

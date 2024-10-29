package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ItemRequestDto {
    private Long id; // уникальный идентификатор запроса

    private String description; // текст запроса, содержащий описание требуемой вещи

    private LocalDateTime created; // дата и время создания запроса
}

package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestInfoDto {
    private Long id; // уникальный идентификатор запроса

    private String description; // текст запроса, содержащий описание требуемой вещи

    private LocalDateTime created; // дата и время создания запроса

    private List<ItemDto> items; // список вещей (ответов на запрос)
}

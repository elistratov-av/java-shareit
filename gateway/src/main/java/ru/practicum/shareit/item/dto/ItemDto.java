package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
public class ItemDto {
    private Long id;
    private final String name;
    private final String description;
    private final Boolean available;
    private Long requestId;
}

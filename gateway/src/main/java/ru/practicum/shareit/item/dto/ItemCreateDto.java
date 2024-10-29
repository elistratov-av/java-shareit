package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
public class ItemCreateDto {
    private Long id;
    @NotBlank(message = "Краткое название вещи не заполнено")
    private final String name;
    @NotBlank(message = "Развёрнутое описание вещи не заполнено")
    private final String description;
    @NotNull(message = "Статус доступности для аренды вещи не заполнен")
    private final Boolean available;
    private Long requestId;
}

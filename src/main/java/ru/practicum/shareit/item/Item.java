package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
public class Item {
    private Long id; // уникальный идентификатор вещи
    @NotBlank
    private String name; // краткое название
    @NotBlank
    private String description; // развёрнутое описание
    @NotNull
    private Boolean available; // статус о том, доступна или нет вещь для аренды
    @NotNull
    private User owner; // владелец вещи
    // если вещь была создана по запросу другого пользователя,
    // то в этом поле будет храниться ссылка на соответствующий запрос
    private ItemRequest request;
}

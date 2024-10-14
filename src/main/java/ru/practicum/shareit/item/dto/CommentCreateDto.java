package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentCreateDto {
    @NotBlank(message = "Текст комментария не заполнен")
    @Size(max = 1024)
    private String text; // текст комментария
}

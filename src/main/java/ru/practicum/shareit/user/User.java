package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
public class User {
    private Long id; // уникальный идентификатор пользователя
    @NotBlank
    private String name; // имя или логин пользователя
    // адрес электронной почты
    // (учтите, что два пользователя не могут иметь одинаковый адрес электронной почты)
    @Email
    @NotBlank
    private String email;
}

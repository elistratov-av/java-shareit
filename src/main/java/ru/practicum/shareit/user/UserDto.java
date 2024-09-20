package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
public class UserDto {
    private Long id;
    private final String name;
    @Email
    private final String email;
}

package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

public interface UserService {
    UserDto get(long id);

    UserDto add(UserCreateDto newUser);

    UserDto update(UserUpdateDto newUser);

    void delete(long id);
}

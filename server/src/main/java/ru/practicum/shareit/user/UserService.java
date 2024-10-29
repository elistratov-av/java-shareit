package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {
    UserDto get(long id);

    UserDto add(UserDto newUser);

    UserDto update(UserDto newUser);

    void delete(long id);
}

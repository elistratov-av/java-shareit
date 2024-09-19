package ru.practicum.shareit.user;

public interface UserService {
    UserDto get(long id);

    UserDto add(UserDto newUser);

    UserDto update(UserDto newUser);

    void delete(long id);
}

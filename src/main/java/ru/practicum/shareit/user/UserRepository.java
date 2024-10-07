package ru.practicum.shareit.user;

import java.util.Optional;

public interface UserRepository {
    Optional<User> get(long id);

    boolean isEmailUsed(String email);

    User add(User newUser);

    User update(User newUser);

    void delete(User user);
}

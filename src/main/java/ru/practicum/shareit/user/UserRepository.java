package ru.practicum.shareit.user;

import java.util.Optional;

public interface UserRepository {
    Optional<User> get(long id);

    boolean isEmailUsed(String email);

    boolean isEmailUsed(String email, long ignoredUserId);

    User add(User newUser);

    User update(User newUser);

    void delete(long id);
}

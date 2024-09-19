package ru.practicum.shareit.user.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserMemRepository implements UserRepository {
    private long idCounter = 0L;
    private final Map<Long, User> users = new HashMap<>();

    protected long nextId() {
        return ++idCounter;
    }

    @Override
    public Optional<User> get(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public boolean isEmailUsed(String email) {
        return users.values().stream()
                .anyMatch(u -> email.equals(u.getEmail()));
    }

    @Override
    public boolean isEmailUsed(String email, long ignoredUserId) {
        return users.values().stream()
                .anyMatch(u -> ignoredUserId != u.getId() && email.equals(u.getEmail()));
    }

    @Override
    public User add(User newUser) {
        // формируем дополнительные данные
        newUser.setId(nextId());
        // сохраняем нового пользователя в памяти приложения
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public User update(User newUser) {
        // проверяем необходимые условия
        User oldUser = get(newUser.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден"));

        // если пользователь найден и все условия соблюдены, обновляем его содержимое
        if (StringUtils.isNoneBlank(newUser.getName())) oldUser.setName(newUser.getName());
        if (StringUtils.isNoneBlank(newUser.getEmail())) oldUser.setEmail(newUser.getEmail());
        return oldUser;
    }

    @Override
    public void delete(long id) {
        users.remove(id);
    }
}

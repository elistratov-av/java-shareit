package ru.practicum.shareit.user.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
public class UserMemRepository implements UserRepository {
    private long idCounter = 0L;
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();

    protected long nextId() {
        return ++idCounter;
    }

    @Override
    public Optional<User> get(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public boolean isEmailUsed(String email) {
        return emails.contains(email);
    }

    @Override
    public User add(User newUser) {
        if (isEmailUsed(newUser.getEmail()))
            throw new DuplicatedDataException("Эл. почта " + newUser.getEmail() + " уже используется");

        // формируем дополнительные данные
        newUser.setId(nextId());
        // сохраняем нового пользователя в памяти приложения
        users.put(newUser.getId(), newUser);
        emails.add(newUser.getEmail());
        return newUser;
    }

    @Override
    public User update(User newUser) {
        // проверяем необходимые условия
        User oldUser = get(newUser.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден"));

        // если пользователь найден и все условия соблюдены, обновляем его содержимое
        if (StringUtils.isNoneBlank(newUser.getName())) oldUser.setName(newUser.getName());
        if (StringUtils.isNoneBlank(newUser.getEmail()) && !newUser.getEmail().equals(oldUser.getEmail())) {
            if (isEmailUsed(newUser.getEmail()))
                throw new DuplicatedDataException("Эл. почта " + newUser.getEmail() + " уже используется");
            emails.remove(oldUser.getEmail());
            oldUser.setEmail(newUser.getEmail());
            emails.add(newUser.getEmail());
        }
        return oldUser;
    }

    @Override
    public void delete(User user) {
        users.remove(user.getId());
        emails.remove(user.getEmail());
    }
}

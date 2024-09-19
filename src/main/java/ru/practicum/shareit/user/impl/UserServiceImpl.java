package ru.practicum.shareit.user.impl;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public UserDto get(long id) {
        User user = userRepository.get(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден"));
        return UserMapper.INSTANCE.toUserDto(user);
    }

    @Override
    public UserDto add(UserDto newUser) {
        if (StringUtils.isBlank(newUser.getEmail()))
            throw new ValidationException("Эл. почта пользователя не задана");
        if (userRepository.isEmailUsed(newUser.getEmail()))
            throw new DuplicatedDataException("Эл. почта " + newUser.getEmail() + " уже используется");

        User user = userRepository.add(UserMapper.INSTANCE.toUser(newUser));
        return UserMapper.INSTANCE.toUserDto(user);
    }

    @Override
    public UserDto update(UserDto newUser) {
        if (newUser.getId() == null)
            throw new ValidationException("Id пользователя должен быть указан");
        if (StringUtils.isNotBlank(newUser.getEmail())
                && userRepository.isEmailUsed(newUser.getEmail(), newUser.getId()))
            throw new DuplicatedDataException("Эл. почта " + newUser.getEmail() + " уже используется");

        User user = userRepository.update(UserMapper.INSTANCE.toUser(newUser));
        return UserMapper.INSTANCE.toUserDto(user);
    }

    @Override
    public void delete(long id) {
        userRepository.get(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден"));

        itemRepository.deleteByOwnerId(id);
        userRepository.delete(id);
    }
}

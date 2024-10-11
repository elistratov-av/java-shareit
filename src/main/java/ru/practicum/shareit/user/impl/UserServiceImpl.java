package ru.practicum.shareit.user.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto get(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден"));
        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto add(UserCreateDto newUser) {
        if (userRepository.existsByEmail(newUser.getEmail()))
            throw new DuplicatedDataException("Эл. почта " + newUser.getEmail() + " уже используется");

        User user = userRepository.save(userMapper.toUser(newUser));
        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto update(UserUpdateDto newUser) {
        // проверяем необходимые условия
        User oldUser = userRepository.findById(newUser.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден"));

        // если пользователь найден и все условия соблюдены, обновляем его содержимое
        if (StringUtils.isNoneBlank(newUser.getName())) oldUser.setName(newUser.getName());
        if (StringUtils.isNoneBlank(newUser.getEmail()) && !newUser.getEmail().equals(oldUser.getEmail())) {
            if (userRepository.existsByEmail(newUser.getEmail()))
                throw new DuplicatedDataException("Эл. почта " + newUser.getEmail() + " уже используется");
            oldUser.setEmail(newUser.getEmail());
        }

        User user = userRepository.save(oldUser);
        return userMapper.toUserDto(user);
    }

    @Override
    public void delete(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден"));
        itemRepository.deleteByOwnerId(id);
        userRepository.delete(user);
    }
}

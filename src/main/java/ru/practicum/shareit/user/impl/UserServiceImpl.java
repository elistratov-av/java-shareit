package ru.practicum.shareit.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
        User user = userRepository.get(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден"));
        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto add(UserCreateDto newUser) {
        User user = userRepository.add(userMapper.toUser(newUser));
        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto update(UserUpdateDto newUser) {
        User user = userRepository.update(userMapper.toUser(newUser));
        return userMapper.toUserDto(user);
    }

    @Override
    public void delete(long id) {
        User user = userRepository.get(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден"));
        itemRepository.deleteByOwnerId(id);
        userRepository.delete(user);
    }
}

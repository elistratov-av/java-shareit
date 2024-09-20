package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public UserDto get(@PathVariable int userId) {
        UserDto user = userService.get(userId);
        log.info("Получен пользователь: {}", user);
        return user;
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto user) {
        user = userService.add(user);
        log.info("Создан пользователь: {}", user);
        return user;
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable long userId, @Valid @RequestBody UserDto newUser) {
        newUser.setId(userId);
        UserDto user = userService.update(newUser);
        log.info("Изменен пользователь: {}", user);
        return user;
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable long userId) {
        userService.delete(userId);
        log.info("Пользователь с id = {} удален", userId);
    }
}

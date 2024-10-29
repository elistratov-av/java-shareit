package ru.practicum.shareit.user;

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
import ru.practicum.shareit.user.dto.UserDto;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public UserDto get(@PathVariable int userId) {
        log.info("==> findById userId = {}", userId);
        UserDto user = userService.get(userId);
        log.info("<== findById user: {}", user);
        return user;
    }

    @PostMapping
    public UserDto create(@RequestBody UserDto newUser) {
        log.info("==> create user: {}", newUser);
        UserDto user = userService.add(newUser);
        log.info("<== create user: {}", user);
        return user;
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable long userId, @RequestBody UserDto newUser) {
        log.info("==> update user: {}", newUser);
        newUser.setId(userId);
        UserDto user = userService.update(newUser);
        log.info("<== update user: {}", user);
        return user;
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable long userId) {
        log.info("==> delete userId = {}", userId);
        userService.delete(userId);
        log.info("<== delete userId = {}", userId);
    }
}

package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserClient userClient;

    @GetMapping("/{userId}")
    public ResponseEntity<Object> get(@PathVariable int userId) {
        log.info("==> get userId = {}", userId);
        ResponseEntity<Object> user = userClient.get(userId);
        log.info("<== get user: {}", user);
        return user;
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid UserCreateDto newUser) {
        log.info("==> create user: {}", newUser);
        ResponseEntity<Object> user = userClient.add(newUser);
        log.info("<== create user: {}", user);
        return user;
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@PathVariable long userId, @RequestBody @Valid UserUpdateDto newUser) {
        log.info("==> update user: {}", newUser);
        newUser.setId(userId);
        ResponseEntity<Object> user = userClient.update(newUser);
        log.info("<== update user: {}", user);
        return user;
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable long userId) {
        log.info("==> delete userId = {}", userId);
        userClient.delete(userId);
        log.info("<== delete userId = {}", userId);
    }
}

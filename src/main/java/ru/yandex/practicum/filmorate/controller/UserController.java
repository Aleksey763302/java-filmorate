package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.Validate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        Validate.validateUser(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.trace("добавлен новый пользователь {}", user.getLogin());
        return user;
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        Validate.validateUser(user);
        if (!users.containsKey(user.getId())) {
            log.debug("не найден пользователь с ID {}", user.getId());
            return ResponseEntity.status(404).body(user);
        }
        users.put(user.getId(), user);
        log.trace("обновление данных пользователя {}", user.getLogin());
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public Collection<User> getUsers() {
        log.trace("получен запрос на получение списка пользователей");
        return users.values();
    }


    private int getNextId() {
        int foundMaxId = users.keySet().stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++foundMaxId;
    }
}

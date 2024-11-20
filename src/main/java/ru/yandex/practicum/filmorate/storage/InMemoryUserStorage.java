package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public void addUser(final User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.debug("добавлен новый пользователь {}", user.getLogin());
    }

    @Override
    public void deleteUser(final int userId) {
        if (foundUser(userId)) {
            users.remove(userId);
            log.debug("пользователь с id {} удален", userId);
        }
    }

    @Override
    public Optional<User> updateUser(final User newUser) {
        if (!foundUser(newUser.getId())) {
            return Optional.empty();
        }
        users.put(newUser.getId(), newUser);
        log.debug("обновление данных пользователя {}", newUser.getLogin());
        return Optional.of(users.get(newUser.getId()));
    }

    @Override
    public Optional<User> getUserById(final int userId) {
        if (!foundUser(userId)) {
            return Optional.empty();
        }
        return Optional.of(users.get(userId));
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    private boolean foundUser(final int userId) {
        if (!users.containsKey(userId)) {
            log.debug("не найден пользователь с ID {}", userId);
            return false;
        }
        return true;
    }

    private int getNextId() {
        int foundMaxId = users.keySet().stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++foundMaxId;
    }
}

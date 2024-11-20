package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    void addUser(User user);
    void deleteUser(int userId);
    Optional<User> updateUser(User newUser);
    Optional<User> getUserById(int userId);
    Collection<User> getAllUsers();
}

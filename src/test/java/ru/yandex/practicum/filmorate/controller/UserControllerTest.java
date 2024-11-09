package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    User user;
    UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController();
        user = User.builder().email("qwerty@ya.ru")
                .login("jktu")
                .birthday(LocalDate.of(1990, 1, 23))
                .name("Андрей")
                .build();
    }

    @Test
    void testCreateUser() {
        userController.createUser(user);
        assertEquals(1, userController.getUsers().size(), "пользователь не добавился");
    }

    @Test
    void testCreateUserWithNoValidEmail() {
        user.setEmail("qwerty");
        try {
            userController.createUser(user);
            assertNotEquals(1, userController.getUsers().size());
        } catch (ValidationException e) {
            assertEquals("электронная почта не может быть пустой и должна содержать символ @", e.getMessage());
        }
    }

    @Test
    void testCreateUserWithNoValidBirthday() {
        user.setBirthday(LocalDate.of(2100, 1, 1));
        try {
            userController.createUser(user);
            assertNotEquals(1, userController.getUsers().size());
        } catch (ValidationException e) {
            assertEquals("дата рождения не может быть в будущем", e.getMessage());
        }
    }

    @Test
    void testCreateNoNameUser() {
        user.setName(null);
        userController.createUser(user);
        Optional<User> optionalUser = userController.getUsers().stream().findFirst();
        User newUser;
        if (optionalUser.isPresent()) {
            newUser = optionalUser.get();
            assertEquals("jktu", newUser.getName(), "имя не изменено");
        }
        assertTrue(optionalUser.isPresent(), "пользователь не найден");
    }
}
package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class Validate {
    public static void validateUser(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            log.trace("введен неправильный формат Email");
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.trace("имя пользователя изменено на логин");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.trace("дата рождения еще не произошла {}", user.getBirthday());
            throw new ValidationException("дата рождения не может быть в будущем");
        }
    }

    public static void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.trace("не задано название фильма: {}", film.getName());
            throw new ValidationException("имя не может быть пустым");
        }
        if (film.getDescription() != null) {
            if (film.getDescription().length() > 200) {
                log.trace("превышена длинна описания: {}", film.getDescription().length());
                throw new ValidationException("максимальная длина описания — 200 символов");
            }
        }
        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
            log.trace("дата релиза не соответствует условию: {}", film.getReleaseDate());
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() < 0) {
            log.trace("продолжительность не соответствует условию: {}", film.getDuration());
            throw new ValidationException("продолжительность фильма должна быть положительным числом.");
        }
    }
}

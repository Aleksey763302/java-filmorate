package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        filmValidate(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("добавлен новый фильм {}", film.getName());
        return films.get(film.getId());
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            log.trace("фильм по id не найден");
            return ResponseEntity.status(404).body(film);
        }
        filmValidate(film);
        films.put(film.getId(), film);
        log.trace("обновлены данные о фильме{}", film.getName());
        return ResponseEntity.ok(film);
    }

    @GetMapping
    public Collection<Film> getFilms() {
        log.trace("получен запрос на показ списка фильмов");
        return films.values();
    }

    private void filmValidate(Film film) {
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

    private int getNextId() {
        int foundMaxId = films.keySet().stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++foundMaxId;
    }
}

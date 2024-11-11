package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.Validate;

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
        Validate.validateFilm(film);
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
        Validate.validateFilm(film);
        films.put(film.getId(), film);
        log.trace("обновлены данные о фильме{}", film.getName());
        return ResponseEntity.ok(film);
    }

    @GetMapping
    public Collection<Film> getFilms() {
        log.trace("получен запрос на показ списка фильмов");
        return films.values();
    }


    private int getNextId() {
        int foundMaxId = films.keySet().stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++foundMaxId;
    }
}

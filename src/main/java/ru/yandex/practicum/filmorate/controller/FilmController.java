package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService service;

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        return service.createFilm(film);
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable("id") final int filmId) {
        service.deleteFilm(filmId);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        return service.updateFilm(film);
    }

    @GetMapping("/popular")
    public Collection<Film> getFilms(@RequestParam(defaultValue = "10") String count) {
        return service.getPopularFilms(count);
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        return service.getAllFilms();
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") final int filmId, @PathVariable final int userId) {
        service.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") final int filmId, @PathVariable final int userId) {
        service.deleteLike(filmId, userId);
    }

}

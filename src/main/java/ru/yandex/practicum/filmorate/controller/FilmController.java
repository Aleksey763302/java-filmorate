package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.RequestCreateFilm;
import ru.yandex.practicum.filmorate.model.RequestUpdateFilm;
import ru.yandex.practicum.filmorate.model.dto.FilmDto;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService service;

    @PostMapping
    public Optional<FilmDto> createFilm(@RequestBody RequestCreateFilm request) {
        return service.createFilm(request);
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable("id") final int filmId) {
        service.deleteFilm(filmId);
    }

    @PutMapping
    public Optional<FilmDto> updateFilm(@RequestBody RequestUpdateFilm request) {
        return service.updateFilm(request);
    }

    @GetMapping("/popular")
    public Optional<List<FilmDto>>  getFilms(@RequestParam(defaultValue = "10") String count) {
        return service.getPopularFilms(count);
    }

    @GetMapping("{filmId}")
    public Optional<FilmDto> getFilmById(@PathVariable("filmId") Integer id) {
        return service.getFilmById(id);
    }

    @GetMapping
    public Optional<List<FilmDto>> getAllFilms() {
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
package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService service;

    @GetMapping("/{genreId}")
    public Optional<Genre> getGenre(@PathVariable("genreId") String id) {
        return service.getGenreById(Integer.parseInt(id));
    }

    @GetMapping
    public Optional<List<Genre>> getAllGenre() {
        return service.getGenres();
    }
}
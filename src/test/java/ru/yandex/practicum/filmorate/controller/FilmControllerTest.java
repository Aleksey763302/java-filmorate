package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTest {
    FilmController filmController;
    Film film;

    @BeforeEach
    void setUp() throws IOException {
        FilmService filmService = new FilmService(new InMemoryFilmStorage(),new InMemoryUserStorage());
        filmController = new FilmController(filmService);
        film = Film.builder().name("very good film")
                .description("film description")
                .releaseDate(LocalDate.parse("1999-04-12"))
                .duration(280).build();
    }

    @Test
    void testCreateFilm() throws IOException, InterruptedException {
        filmController.createFilm(film);
        assertEquals(1, filmController.getFilms("10").size(), "фильм не был добавлен");
    }

    @Test
    void testCreateFilmWithNoValidName() {
        film.setName("");
        try {
            filmController.createFilm(film);
            assertNotEquals(1, filmController.getFilms("10").size());
        } catch (ValidationException e) {
            assertEquals("имя не может быть пустым", e.getReason());
        }

    }

    @Test
    void testCreateFilmWithNoValidDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("description".repeat(20));
        try {
            film.setDescription(new String(sb));
            assertNotEquals(1, filmController.getFilms("10").size());
        } catch (ValidationException e) {
            assertEquals("максимальная длина описания — 200 символов", e.getReason());
        }
    }

    @Test
    void testCreateFilmWithNoValidDateRelease() {
        film.setReleaseDate(LocalDate.of(1800, 10, 10));
        try {
            filmController.createFilm(film);
            assertNotEquals(1, filmController.getFilms("10").size());
        } catch (ValidationException e) {
            assertEquals("дата релиза — не раньше 28 декабря 1895 года", e.getReason());
        }
    }

    @Test
    void testCreateFilmWithNoValidDuration() {
        film.setDuration(-100);
        try {
            filmController.createFilm(film);
            assertNotEquals(1, filmController.getFilms("10").size());
        } catch (ValidationException e) {
            assertEquals("продолжительность фильма должна быть положительным числом.", e.getReason());
        }
    }
}
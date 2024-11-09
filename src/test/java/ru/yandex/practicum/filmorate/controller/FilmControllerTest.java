package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTest {
    FilmController filmController;
    Film film;

    @BeforeEach
    void setUp() throws IOException {
        filmController = new FilmController();
        film = Film.builder().name("very good film")
                .description("film description")
                .releaseDate(LocalDate.parse("1999-04-12"))
                .duration(Duration.ofMinutes(280)).build();
    }


    @Test
    void testCreateFilm() throws IOException, InterruptedException {
        filmController.createFilm(film);
        assertEquals(1, filmController.getFilms().size(), "фильм не был добавлен");
    }

    @Test
    void testCreateFilmWithNoValidName() {
        film.setName("");
        try {
            filmController.createFilm(film);
            assertNotEquals(1, filmController.getFilms().size());
        } catch (ValidationException e) {
            assertEquals("имя не может быть пустым", e.getMessage());
        }

    }

    @Test
    void testCreateFilmWithNoValidDescription() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            sb.append("description");
        }
        try {
            film.setDescription(new String(sb));
            assertNotEquals(1, filmController.getFilms().size());
        } catch (ValidationException e) {
            assertEquals("максимальная длина описания — 200 символов", e.getMessage());
        }
    }

    @Test
    void testCreateFilmWithNoValidDateRelease() {
        film.setReleaseDate(LocalDate.of(1800, 10, 10));
        try {
            filmController.createFilm(film);
            assertNotEquals(1, filmController.getFilms().size());
        } catch (ValidationException e) {
            assertEquals("дата релиза — не раньше 28 декабря 1895 года", e.getMessage());
        }
    }
    @Test
    void testCreateFilmWithNoValidDuration(){
        film.setDuration(Duration.ofMinutes(-100));
        try{
            filmController.createFilm(film);
            assertNotEquals(1,filmController.getFilms().size());
        }catch (ValidationException e){
            assertEquals("продолжительность фильма должна быть положительным числом.",e.getMessage());
        }
    }
}
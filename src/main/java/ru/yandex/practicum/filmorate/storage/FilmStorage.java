package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    void addFilm(Film film);
    void deleteFilm(int filmId);
    Optional<Film> updateFilm(Film newfilm);
    Optional<Film> getFilmById(int filmId);
    Collection<Film> getAllFilms();
}

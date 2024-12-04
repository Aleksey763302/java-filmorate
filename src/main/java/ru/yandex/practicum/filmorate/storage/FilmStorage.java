package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface FilmStorage {
    void addFilm(Film film, boolean isCreate);

    void updateLikes(Integer filmId, List<Integer> likes);

    void deleteFilm(int filmId, boolean isDeleteLikes);

    Film getFilmById(int filmId);

    List<Integer> getLikesFilm(int filmId);

    Collection<Film> getAllFilms();

    Map<Integer, List<Integer>> getFilms();
}

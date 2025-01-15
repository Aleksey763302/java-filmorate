package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.RequestCreateFilm;
import ru.yandex.practicum.filmorate.model.dto.FilmDto;
import ru.yandex.practicum.filmorate.storage.database.response.ResponseFilm;

import java.util.List;
import java.util.Map;

public interface FilmStorage {
    ResponseFilm createFilm(RequestCreateFilm request);

    ResponseFilm updateFilm(RequestCreateFilm request);

    void addLike(int filmID, int userID);

    void deleteLike(int filmID, int userID);

    void deleteFilm(int filmID);

    ResponseFilm getFilmById(int filmID);

    List<Integer> getLikesFilm(int filmID);

    List<FilmDto> getAllFilms();

    Map<Integer, List<Integer>> getLikesFilms();
}

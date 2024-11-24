package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundFilmException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private final Map<Integer, List<Integer>> likes = new HashMap<>();

    @Override
    public void addFilm(final Film film, boolean isCreate) {
        if (isCreate) {
            film.setId(getNextId());
        }
        films.put(film.getId(), film);
        likes.put(film.getId(), new ArrayList<>());
    }

    @Override
    public void updateLikes(final Integer filmId, final List<Integer> likes) {
        this.likes.put(filmId, likes);
    }

    @Override
    public Map<Integer, List<Integer>> getFilms() {
        return likes;
    }


    @Override
    public void deleteFilm(final int filmId, boolean isDeleteLikes) {
        if (!films.containsKey(filmId)) {
            throw new NotFoundFilmException();
        }
        films.remove(filmId);
        if (isDeleteLikes) {
            likes.remove(filmId);
        }
    }


    @Override
    public Film getFilmById(final int filmId) {
        if (!films.containsKey(filmId)) {
            throw new NotFoundFilmException();
        }
        return films.get(filmId);
    }

    @Override
    public List<Integer> getLikesFilm(final int filmId) {
        if (!likes.containsKey(filmId)) {
            throw new NotFoundFilmException();
        }
        return likes.get(filmId);
    }

    @Override
    public Collection<Film> getAllFilms() {
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

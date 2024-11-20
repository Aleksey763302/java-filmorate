package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public void addFilm(final Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.debug("добавлен новый фильм {}", film.getName());
    }

    @Override
    public void deleteFilm(final int filmId) {
        if (foundFilm(filmId)) {
            films.remove(filmId);
            log.debug("фильм с id: {} удален", filmId);
        }
    }

    @Override
    public Optional<Film> updateFilm(final Film newfilm) {
        if (foundFilm(newfilm.getId())) {
            films.put(newfilm.getId(), newfilm);
            log.trace("обновлены данные о фильме: {}", newfilm.getName());
            return Optional.of(films.get(newfilm.getId()));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Film> getFilmById(int filmId) {
        if (foundFilm(filmId)) {
            return Optional.of(films.get(filmId));
        }
        return Optional.empty();
    }

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }
    
    private boolean foundFilm(final int filmId) {
        if (!films.containsKey(filmId)) {
            log.debug("фильм с id: {} не найден", filmId);
            return false;
        }
        return true;
    }

    private int getNextId() {
        int foundMaxId = films.keySet().stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++foundMaxId;
    }
}

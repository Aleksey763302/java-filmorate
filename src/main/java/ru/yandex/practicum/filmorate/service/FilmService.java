package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundUserException;
import ru.yandex.practicum.filmorate.exceptions.NotValidParamException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage storage;
    private final UserStorage userStorage;

    public Film createFilm(Film film) {
        Validate.validateFilm(film);
        storage.addFilm(film, true);
        log.info("добавлен новый фильм: {}", film.getName());
        return storage.getFilmById(film.getId());
    }

    public Film updateFilm(Film film) {
        Validate.validateFilm(film);
        final int filmId = film.getId();
        storage.deleteFilm(filmId, false);
        storage.addFilm(film, false);
        log.info("обновлены данные о фильме, id фильма {}", film.getId());
        return storage.getFilmById(film.getId());
    }

    public void deleteFilm(final int filmId) {
        storage.deleteFilm(filmId, true);
        log.info("фильм удален, id фильма {}", filmId);
    }

    public void addLike(Integer filmId, Integer userId) {
        if (userStorage.getUserById(userId) == null) {
            throw new NotFoundUserException();
        }
        List<Integer> users = storage.getLikesFilm(filmId);
        if (!users.contains(userId)) {
            users.add(userId);
            storage.updateLikes(filmId, users);
            String logMessage = "добавлен лайк фильму: id фильма: {}, id пользователя: {}";
            log.info(logMessage, storage.getFilmById(filmId).getId(), userId);
        }
    }

    public void deleteLike(Integer filmId, Integer userId) {
        List<Integer> users = storage.getLikesFilm(filmId);
        if (!users.contains(userId)) {
            throw new NotFoundUserException();
        }
        users.remove(userId);
        storage.updateLikes(filmId, users);
        String logMessage = "пользователь удалил лайк фильму, id фильма: {}, id пользователя: {}";
        log.info(logMessage, storage.getFilmById(filmId).getName(), userId);
    }

    public Collection<Film> getAllFilms() {
        return storage.getAllFilms();
    }

    public Collection<Film> getPopularFilms(String countStr) {
        int count;
        try {
            count = Integer.parseInt(countStr);
        } catch (NumberFormatException e) {
            throw new NotValidParamException(e.getMessage());
        }

        Map<Integer, List<Integer>> likes = storage.getFilms();
        TreeSet<Integer> sortedFilms = new TreeSet<>(new Comparator<>() {
            int likes1;
            int likes2;

            @Override
            public int compare(Integer filmOne, Integer filmTwo) {
                likes1 = likes.get(filmOne).size();
                likes2 = likes.get(filmTwo).size();
                return likes2 - likes1;
            }
        });
        sortedFilms.addAll(likes.keySet());

        List<Film> listFilms = sortedFilms.stream().limit(count)
                .map(storage::getFilmById)
                .toList();
        log.trace("сформирован список по запросу getPopularFilms()");
        return listFilms;
    }

}

package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundUserException;
import ru.yandex.practicum.filmorate.exceptions.NotValidParamException;
import ru.yandex.practicum.filmorate.model.ID;
import ru.yandex.practicum.filmorate.model.RequestCreateFilm;
import ru.yandex.practicum.filmorate.model.RequestUpdateFilm;
import ru.yandex.practicum.filmorate.model.dto.FilmDto;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.database.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.database.RatingDbStorage;
import ru.yandex.practicum.filmorate.storage.database.response.ResponseFilm;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    @Qualifier("filmDbStorage")
    private final FilmStorage storage;
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;
    private final GenreDbStorage genreStorage;
    private final RatingDbStorage ratingStorage;

    public Optional<FilmDto> createFilm(RequestCreateFilm requestFilm) {
        Validate.validateFilm(requestFilm.getFilmDto());
        if (requestFilm.getGenres() != null) {
            Validate.validateGenre(genreStorage.getGenres(), requestFilm.getGenres());
        }
        if (requestFilm.getMpa() != null) {
            List<ID> mpaList = new ArrayList<>();
            mpaList.add(requestFilm.getMpa());
            Validate.validateMpa(ratingStorage.getAllMpa(), mpaList);
        }
        ResponseFilm response = storage.createFilm(requestFilm);
        return Optional.ofNullable(response.getFilmDto());
    }

    public Optional<FilmDto> getFilmById(Integer id) {
        return Optional.ofNullable(storage.getFilmById(id).getFilmDto());
    }

    public Optional<FilmDto> updateFilm(RequestUpdateFilm request) {
        Validate.validateFilm(request.getFilmDto());
        ResponseFilm response = storage.updateFilm(request.getRequestFilm());
        return Optional.ofNullable(response.getFilmDto());
    }

    public void deleteFilm(final int filmID) {
        storage.deleteFilm(filmID);
    }

    public void addLike(final int filmID, final int userID) {
        if (userStorage.getUserById(userID) == null) {
            throw new NotFoundUserException();
        }
        if (!storage.getLikesFilm(filmID).contains(userID)) {
            storage.addLike(filmID, userID);
        }
    }

    public void deleteLike(final int filmID, final int userID) {
        if (!storage.getLikesFilm(filmID).contains(userID)) {
            throw new NotFoundUserException();
        }
        storage.deleteLike(filmID, userID);
    }

    public Optional<List<FilmDto>> getAllFilms() {
        return Optional.ofNullable(storage.getAllFilms());
    }

    public Optional<List<FilmDto>> getPopularFilms(String countStr) {
        int count;
        try {
            count = Integer.parseInt(countStr);
        } catch (NumberFormatException e) {
            throw new NotValidParamException(e.getMessage());
        }

        Map<Integer, List<Integer>> likes = storage.getLikesFilms();
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

        return Optional.of(sortedFilms.stream().limit(count)
                .map(storage::getFilmById)
                .map(ResponseFilm::getFilmDto)
                .toList());
    }

}

package ru.yandex.practicum.filmorate.storage.database;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ErrorAddingData;
import ru.yandex.practicum.filmorate.exceptions.NotFoundRating;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.database.mappers.film.MpaRowMapper;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RatingDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaRowMapper mapper;

    public void addMpa(final Integer filmId, final Integer ratingId) {
        String sqlQuery = "INSERT INTO ratings_films (film_Id, rating_id) VALUES (?,?);";
        int result;
        try {
            result = jdbcTemplate.update(sqlQuery, filmId, ratingId);
            if (result == 0) {
                throw new ErrorAddingData("рейтинг не был добавлен");
            }
        } catch (DataAccessException e) {
            log.debug("ошибка при добавлении рейтинга: {}", e.getMessage());
        }
    }

    public List<Mpa> getAllMpa() {
        String sqlQuery = "SELECT rating_id, title FROM ratings;";
        return jdbcTemplate.query(sqlQuery, mapper);
    }

    public Mpa getMpa(Integer ratingId) {
        String sqlQuery = "SELECT rating_id, title FROM ratings WHERE rating_id = ?;";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, mapper, ratingId);
        } catch (DataAccessException e) {
            return null;
        }
    }

    public Mpa getMpaFilm(Integer filmId) throws NotFoundRating {
        String sqlQuery = "SELECT rating_id FROM ratings_films WHERE film_id = ?;";
        Integer ratingId;
        try {
            ratingId = jdbcTemplate.queryForObject(sqlQuery, Integer.class, filmId);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return getMpa(ratingId);
    }
}

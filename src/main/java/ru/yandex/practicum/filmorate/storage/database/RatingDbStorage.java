package ru.yandex.practicum.filmorate.storage.database;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
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
    private final NamedParameterJdbcTemplate jdbc;
    private final MpaRowMapper mapper;

    public void addMpa(final int filmId, final int ratingId) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("filmID", filmId).addValue("ratingID", ratingId);
        String sqlQuery = "INSERT INTO ratings_films (film_Id, rating_id) VALUES (:filmID,:ratingID)";
        int result;
        try {
            result = jdbc.update(sqlQuery, namedParameters);
            if (result == 0) {
                throw new ErrorAddingData("рейтинг не был добавлен");
            }
        } catch (DataAccessException e) {
            log.debug("ошибка при добавлении рейтинга: {}", e.getMessage());
        }
    }

    public List<Mpa> getAllMpa() {
        String sqlQuery = "SELECT rating_id, title FROM ratings;";
        return jdbc.query(sqlQuery, mapper);
    }

    public Mpa getMpa(Integer ratingId) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("ratingID", ratingId);
        String sqlQuery = "SELECT rating_id, title FROM ratings WHERE rating_id = :ratingID";
        try {
            return jdbc.queryForObject(sqlQuery, namedParameters, mapper);
        } catch (DataAccessException e) {
            return null;
        }
    }

    public Mpa getMpaFilm(Integer filmId) throws NotFoundRating {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("filmID", filmId);
        String sqlQuery = "SELECT rating_id FROM ratings_films WHERE film_id = :filmID";
        Integer ratingId;
        try {
            ratingId = jdbc.queryForObject(sqlQuery, namedParameters, Integer.class);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return getMpa(ratingId);
    }
}

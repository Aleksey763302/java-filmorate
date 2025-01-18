package ru.yandex.practicum.filmorate.storage.database;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.database.mappers.film.GenreRowMapper;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GenreDbStorage {
    private final NamedParameterJdbcTemplate jdbc;
    private final GenreRowMapper mapper;

    public void addGenreToFilm(final int filmId, final int genreId) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("filmID", filmId).addValue("genreID", genreId);
        String sqlQuery = "INSERT INTO genres_films (film_Id, genre_id) VALUES (:filmID,:genreID)";
        if (!getGenreFilm(filmId).contains(getGenre(genreId))) {
            try {
                jdbc.update(sqlQuery, namedParameters);
            } catch (DataAccessException e) {
                log.debug("ошибка при добавлении жанра: {}", e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    public List<Genre> getGenres() {
        String sqlQuery = "SELECT genre_id, title FROM genres;";
        return jdbc.query(sqlQuery, mapper);
    }

    public Genre getGenre(final int genreId) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("genreID", genreId);
        String sqlQuery = "SELECT genre_id, title FROM genres WHERE genre_id = :genreID";
        Genre genre = null;
        try {
            genre = jdbc.queryForObject(sqlQuery, namedParameters, mapper);
        } catch (DataAccessException e) {
            log.debug("ошибка при поиске жанра по id: {}", e.getMessage());
        }
        return genre;
    }

    public List<Genre> getGenreFilm(final int filmId) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("filmID", filmId);
        String sqlQuery = "SELECT genre_id FROM genres_films WHERE film_id = :filmID";
        List<Integer> genreId;
        try {
            genreId = jdbc.queryForList(sqlQuery, namedParameters, Integer.class);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        List<Genre> genres = new ArrayList<>();
        for (Integer id : genreId) {
            genres.add(getGenre(id));
        }
        return genres;
    }
}

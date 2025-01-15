package ru.yandex.practicum.filmorate.storage.database;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.database.mappers.film.GenreRowMapper;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreRowMapper mapper;

    public void addGenreToFilm(final Integer filmId, final Integer genreId) {
        String sqlQuery = "INSERT INTO genres_films (film_Id, genre_id) VALUES (?,?);";
        if (!getGenreFilm(filmId).contains(getGenre(genreId))) {
            try {
                jdbcTemplate.update(sqlQuery, filmId, genreId);
            } catch (DataAccessException e) {
                log.debug("ошибка при добавлении жанра: {}", e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    public List<Genre> getGenres() {
        String sqlQuery = "SELECT genre_id, title FROM genres;";
        return jdbcTemplate.query(sqlQuery, mapper);
    }

    public Genre getGenre(final Integer genreId) {
        String sqlQuery = "SELECT genre_id, title FROM genres WHERE genre_id = ?;";
        Genre genre = null;
        try {
            genre = jdbcTemplate.queryForObject(sqlQuery, mapper, genreId);
        } catch (DataAccessException e) {
            log.debug("ошибка при поиске жанра по id: {}", e.getMessage());
        }
        return genre;
    }

    public List<Genre> getGenreFilm(final Integer filmId) {
        String sqlQuery = "SELECT genre_id FROM genres_films WHERE film_id = ?;";
        List<Integer> genreId;
        try {
            genreId = jdbcTemplate.queryForList(sqlQuery, Integer.class, filmId);
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

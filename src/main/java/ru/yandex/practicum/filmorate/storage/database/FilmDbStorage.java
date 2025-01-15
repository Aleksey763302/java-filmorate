package ru.yandex.practicum.filmorate.storage.database;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ErrorAddingData;
import ru.yandex.practicum.filmorate.exceptions.IncorrectGenreID;
import ru.yandex.practicum.filmorate.exceptions.IncorrectMpaID;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.ID;
import ru.yandex.practicum.filmorate.model.RequestCreateFilm;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.database.mappers.film.IDRowMapper;
import ru.yandex.practicum.filmorate.storage.database.mappers.film.LikesRowMapper;
import ru.yandex.practicum.filmorate.storage.database.mappers.film.ResponseFilmRowMapper;
import ru.yandex.practicum.filmorate.storage.database.response.ResponseFilm;
import ru.yandex.practicum.filmorate.model.dto.FilmDto;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.*;

@Slf4j
@Qualifier("filmDbStorage")
@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final ResponseFilmRowMapper responseMapper;
    private final GenreDbStorage genreDbStorage;
    private final RatingDbStorage ratingDbStorage;
    private final LikesRowMapper likesMapper;
    private final IDRowMapper idRowMapper;

    @Override
    public ResponseFilm createFilm(RequestCreateFilm request) {
        String sqlQuery = "INSERT INTO films (name,description,releaseDate,duration) VALUES (?,?,?,?);";
        Integer filmId;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator stm = createPreparedStatement(request, sqlQuery);
        int result;
        try {
            result = jdbcTemplate.update(stm, keyHolder);
            filmId = (Integer) keyHolder.getKey();
            if (result == 0) {
                throw new ErrorAddingData("данные не были добавлены");
            }
        } catch (DataAccessException e) {
            log.debug("ошибка при добавлении фильма: {}", e.getMessage());
            throw new ErrorAddingData(e.getMessage());
        }
        if (request.getMpa() != null) {
            if (ratingDbStorage.getMpa(request.getMpa().getId()) == null) {
                ratingDbStorage.addMpa(filmId, null);
                throw new IncorrectMpaID("неверный id mpa");
            }
            ratingDbStorage.addMpa(filmId, request.getMpa().getId());
        }
        List<ID> genres = request.getGenres();
        if (genres != null) {
            for (ID id : genres) {
                if (genreDbStorage.getGenre(id.getId()) == null) {
                    genreDbStorage.addGenreToFilm(filmId, null);
                    throw new IncorrectGenreID("неверный id жанра");
                }
                genreDbStorage.addGenreToFilm(filmId, id.getId());
            }
        }
        return getFilmById(filmId);
    }

    @Override
    public ResponseFilm updateFilm(RequestCreateFilm request) {
        String sqlQuery = "UPDATE films SET name = ?,description = ?,releaseDate = ?,duration = ? WHERE film_id = ?;";
        PreparedStatementCreator psc = createPreparedStatement(request, sqlQuery);
        final int result;
        try {
            result = jdbcTemplate.update(psc);
        } catch (DataAccessException e) {
            log.debug(e.getMessage());
            throw new RuntimeException(e);
        }

        if (result == 0) {
            log.debug("ошибка при обновлении фильма");
            throw new ErrorAddingData("данные не были обновлены");
        }
        return getFilmById(request.getId());
    }

    @Override
    public void addLike(int filmID, int userID) {
        String sqlQuery = "INSERT INTO likes (film_id,user_id) VALUES (?,?);";
        try {
            jdbcTemplate.update(sqlQuery, filmID, userID);
        } catch (DataAccessException e) {
            log.debug("ошибка при добавлении лайка: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteLike(int filmID, int userID) {
        String sqlQuery = "DELETE FROM likes WHERE film_id = ? AND user_id = ?;";
        try {
            jdbcTemplate.update(sqlQuery, filmID, userID);
        } catch (DataAccessException e) {
            log.debug("ошибка при удалении лайка: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteFilm(int filmID) {
        String sqlQuery = "DELETE FROM films WHERE film_id = ?;";
        try {
            jdbcTemplate.update(sqlQuery, filmID);
        } catch (DataAccessException e) {
            log.debug("ошибка при удалении фильма: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseFilm getFilmById(int filmID) {
        String sqlQuery = "SELECT * FROM films WHERE film_id = ?;";
        ResponseFilm responseFilm;
        try {
            responseFilm = jdbcTemplate.queryForObject(sqlQuery, responseMapper, filmID);
        } catch (DataAccessException e) {
            log.debug("ошибка при получении фильма из БД: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        assert responseFilm != null;
        Mpa rating = ratingDbStorage.getMpaFilm(filmID);
        List<Genre> genre = genreDbStorage.getGenreFilm(filmID);
        responseFilm.setGenres(genre);
        responseFilm.setRating(rating);
        return responseFilm;
    }


    @Override
    public List<Integer> getLikesFilm(int filmID) {
        String sqlQuery = "SELECT film_id, user_id FROM likes WHERE film_id = ?;";
        List<Integer> likes;
        try {
            likes = jdbcTemplate.query(sqlQuery, likesMapper, filmID);
        } catch (DataAccessException e) {
            log.debug("ошибка при получении списка лайков из БД: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        return likes;
    }

    @Override
    public List<FilmDto> getAllFilms() {
        String sqlQuery = "SELECT film_id,name,description,releaseDate,duration FROM films;";
        try {
            return jdbcTemplate.query(sqlQuery, responseMapper).stream()
                    .peek(responseFilm -> responseFilm.setRating(ratingDbStorage.getMpaFilm(responseFilm.getId())))
                    .peek(responseFilm -> responseFilm.setGenres(genreDbStorage.getGenreFilm(responseFilm.getId())))
                    .map(ResponseFilm::getFilmDto).toList();
        } catch (DataAccessException e) {
            log.debug("ошибка при формировании списка фильмов из БД: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<Integer, List<Integer>> getLikesFilms() {
        String getFilmId = "SELECT film_id FROM films;";
        Map<Integer, List<Integer>> response = new HashMap<>();
        try {
            Set<Integer> idFilms = new HashSet<>(new HashSet<>(jdbcTemplate.query(getFilmId, idRowMapper)));
            for (Integer idFilm : idFilms) {
                response.put(idFilm, getLikesFilm(idFilm));
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    private PreparedStatementCreator createPreparedStatement(RequestCreateFilm request, String sqlQuery) {
        return con -> {
            try {
                PreparedStatement preparedStatement = con.prepareStatement(sqlQuery, new String[]{"film_id"});
                preparedStatement.setString(1, request.getName());
                preparedStatement.setString(2, request.getDescription());
                preparedStatement.setDate(3, Date.valueOf(request.getReleaseDate()));
                preparedStatement.setInt(4, request.getDuration());
                if (sqlQuery.contains("film_id")) {
                    preparedStatement.setInt(5, request.getId());
                }
                return preparedStatement;
            } catch (DataAccessException e) {
                log.debug("ошибка при создании PreparedStatement: {}", e.getMessage());
                throw new RuntimeException();
            }
        };
    }
}
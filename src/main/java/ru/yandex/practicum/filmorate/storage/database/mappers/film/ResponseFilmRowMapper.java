package ru.yandex.practicum.filmorate.storage.database.mappers.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.database.response.ResponseFilm;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@Component
public class ResponseFilmRowMapper implements RowMapper<ResponseFilm> {
    @Override
    public ResponseFilm mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResponseFilm response = new ResponseFilm();
        response.setId(rs.getInt("film_id"));
        response.setName(rs.getString("name"));
        response.setDescription(rs.getString("description"));
        response.setDuration(rs.getInt("duration"));
        response.setReleaseDate(rs.getTimestamp("releaseDate").toLocalDateTime().toLocalDate());
        return response;
    }
}

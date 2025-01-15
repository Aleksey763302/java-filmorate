package ru.yandex.practicum.filmorate.storage.database.mappers.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@Component
public class GenreRowMapper implements RowMapper<Genre> {
    @Override
    public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            String genre = rs.getString("title").trim();
            return Genre.builder().id(rs.getInt("genre_id")).name(genre).build();
        } catch (IllegalArgumentException e) {
            log.debug("ошибка при получении жанра по ID {}", e.getMessage());
            throw new RuntimeException(e);
        }

    }
}

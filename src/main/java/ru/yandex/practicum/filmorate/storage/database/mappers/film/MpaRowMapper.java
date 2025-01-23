package ru.yandex.practicum.filmorate.storage.database.mappers.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@Component
public class MpaRowMapper implements RowMapper<Mpa> {
    @Override
    public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            String mpa = rs.getString("title").trim();
            return Mpa.builder().id(rs.getInt("rating_id")).name(mpa).build();
        } catch (IllegalArgumentException e) {
            log.debug("ошибка при получении рейтинга по id {}", e.getMessage());
            throw new RuntimeException(e);
        }

    }
}

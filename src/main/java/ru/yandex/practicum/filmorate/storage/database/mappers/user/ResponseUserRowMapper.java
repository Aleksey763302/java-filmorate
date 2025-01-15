package ru.yandex.practicum.filmorate.storage.database.mappers.user;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.database.response.ResponseUser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;

@Component
public class ResponseUserRowMapper implements RowMapper<ResponseUser> {

    @Override
    public ResponseUser mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return ResponseUser.builder()
                .id(resultSet.getInt("user_id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(LocalDate.ofInstant(resultSet
                        .getTimestamp("birthday").toInstant(), ZoneId.systemDefault()))
                .build();
    }
}

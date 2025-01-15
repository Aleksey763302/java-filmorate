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
import ru.yandex.practicum.filmorate.exceptions.NotFoundUserException;
import ru.yandex.practicum.filmorate.model.RequestUser;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.database.mappers.user.FriendsRowMapper;
import ru.yandex.practicum.filmorate.storage.database.mappers.user.ResponseUserRowMapper;
import ru.yandex.practicum.filmorate.storage.database.response.ResponseUser;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

@Slf4j
@Repository
@Qualifier("userDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final ResponseUserRowMapper mapper;
    private final FriendsRowMapper friendMapper;

    @Override
    public ResponseUser createUser(RequestUser request) {
        String sqlQuery = "INSERT INTO users (email,login,name,birthday) VALUES (?,?,?,?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator psc = getPreparedStatement(request, sqlQuery);
        int result;
        result = jdbcTemplate.update(psc, keyHolder);
        if (result == 0) {
            throw new ErrorAddingData("пользователь не был добавлен");
        }
        Integer userId = (Integer) keyHolder.getKey();
        if (userId != null) {
            return getUserById(userId);
        } else {
            throw new ErrorAddingData("из БД не был возвращен ID нового пользователя");
        }
    }

    @Override
    public ResponseUser updateUser(RequestUser request) {
        String sqlQuery = "UPDATE users SET email = ?,login = ?, name = ?,birthday = ? WHERE user_id = ?;";
        PreparedStatementCreator psc = getPreparedStatement(request, sqlQuery);
        int result = 0;
        try {
            result = jdbcTemplate.update(psc);
        } catch (DataAccessException e) {
            log.debug("ошибка при обращении к БД: {}", e.getMessage());
        }
        if (result == 0) {
            throw new ErrorAddingData("данные о пользователе не были изменены");
        }
        return getUserById(request.getId());
    }

    @Override
    public void deleteUser(int userId) {
        String sqlQuery = "DELETE FROM users WHERE user_id = ?;";
        jdbcTemplate.update(sqlQuery, userId);
    }

    @Override
    public ResponseUser getUserById(int userID) {
        String sqlQuery = "SELECT * FROM users WHERE user_id = ?;";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, mapper, userID);
        } catch (DataAccessException e) {
            throw new NotFoundUserException();
        }
    }

    @Override
    public List<ResponseUser> getAllUsers() {
        String sqlQuery = "SELECT * FROM users;";
        return jdbcTemplate.query(sqlQuery, mapper);
    }

    @Override
    public void addFriend(int userID, int friendID) {
        String sqlQuery = "INSERT INTO friends (user_id,friend_id) VALUES (?,?);";
        try {
            jdbcTemplate.update(sqlQuery, userID, friendID);
        } catch (DataAccessException e) {
            log.debug("ошибка при добавлении пользователя в друзья: {}", e.getMessage());
        }

    }

    @Override
    public void deleteFriend(int userID, int friendID) {
        String sqlQuery = "DELETE FROM friends WHERE user_id = ?;";
        try {
            jdbcTemplate.update(sqlQuery, userID);
        } catch (DataAccessException e) {
            log.debug("ошибка при удалении друзей: {}", e.getMessage());
        }
    }

    @Override
    public List<Integer> getListFriends(Integer userid) {
        String sklQuery = "SELECT friend_id FROM friends WHERE user_id = ?;";
        return jdbcTemplate.query(sklQuery, friendMapper, userid);
    }


    private PreparedStatementCreator getPreparedStatement(RequestUser request, String sqlQuery) {
        return con -> {
            PreparedStatement stm = con.prepareStatement(sqlQuery, new String[]{"user_id"});
            stm.setString(1, request.getEmail());
            stm.setString(2, request.getLogin());
            stm.setString(3, request.getName());
            stm.setDate(4, Date.valueOf(request.getBirthday()));
            if (request.getId() != null) {
                stm.setInt(5, request.getId());
            }
            return stm;
        };
    }
}

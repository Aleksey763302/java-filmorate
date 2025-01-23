package ru.yandex.practicum.filmorate.storage.database;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
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

import java.util.List;
import java.util.Objects;

@Slf4j
@Repository
@Qualifier("userDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final NamedParameterJdbcTemplate jdbc;
    private final ResponseUserRowMapper mapper;
    private final FriendsRowMapper friendMapper;

    @Override
    public ResponseUser createUser(RequestUser request) {
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(request.getUserDto());
        String sqlQuery = "INSERT INTO users (email,login,name,birthday) VALUES (:email,:login,:name,:birthday)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int[] result;
        result = jdbc.batchUpdate(sqlQuery, batch, keyHolder);
        if (result.length == 0) {
            throw new ErrorAddingData("пользователь не был добавлен");
        }
        Integer userId = (Integer) keyHolder.getKey();
        if (Objects.nonNull(userId)) {
            return getUserById(userId);
        } else {
            throw new ErrorAddingData("из БД не был возвращен ID нового пользователя");
        }
    }

    @Override
    public ResponseUser updateUser(RequestUser request) {
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(request.getUserDto());
        String sqlQuery = "UPDATE users " +
                "SET email = :email, login = :login, name = :name, birthday = :birthday " +
                "WHERE user_id = " + request.getId();
        int[] result;
        try {
            result = jdbc.batchUpdate(sqlQuery, batch);
        } catch (DataAccessException e) {
            log.debug("ошибка при обращении к БД: {}", e.getMessage());
            throw new ErrorAddingData(e.getMessage());
        }
        if (result.length == 0) {
            throw new ErrorAddingData("данные о пользователе не были изменены");
        }
        return getUserById(request.getId());
    }

    @Override
    public void deleteUser(int userId) {
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("userID", userId);
        String sqlQuery = "DELETE FROM users WHERE user_id = :userID";
        jdbc.update(sqlQuery, namedParameters);
    }

    @Override
    public ResponseUser getUserById(int userID) {
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("userID", userID);
        String sqlQuery = "SELECT * FROM users WHERE user_id = :userID";
        try {
            return jdbc.queryForObject(sqlQuery, namedParameters, mapper);
        } catch (DataAccessException e) {
            throw new NotFoundUserException();
        }
    }

    @Override
    public List<ResponseUser> getAllUsers() {
        String sqlQuery = "SELECT * FROM users;";
        return jdbc.query(sqlQuery, mapper);
    }

    @Override
    public void addFriend(int userID, int friendID) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("userID", userID).addValue("friendID", friendID);
        String sqlQuery = "INSERT INTO friends (user_id,friend_id) VALUES (:userID,:friendID);";
        try {
            jdbc.update(sqlQuery, namedParameters);
        } catch (DataAccessException e) {
            log.debug("ошибка при добавлении пользователя в друзья: {}", e.getMessage());
        }
    }

    @Override
    public void deleteFriend(int userID, int friendID) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("userID", userID).addValue("friendID", friendID);
        String sqlQuery = "DELETE FROM friends WHERE user_id = :userID AND :friendID";
        try {
            jdbc.update(sqlQuery, namedParameters);
        } catch (DataAccessException e) {
            log.debug("ошибка при удалении друзей: {}", e.getMessage());
        }
    }

    @Override
    public List<Integer> getListFriends(Integer userid) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("userID", userid);
        String sklQuery = "SELECT friend_id FROM friends WHERE user_id = :userID";
        return jdbc.query(sklQuery,namedParameters, friendMapper);
    }
}

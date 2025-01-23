package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundUserException;
import ru.yandex.practicum.filmorate.model.RequestUser;
import ru.yandex.practicum.filmorate.model.dto.UserDto;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.database.response.ResponseUser;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    @Qualifier("userDbStorage")
    private final UserStorage storage;

    public Optional<UserDto> createUser(RequestUser request) {
        Validate.validateUser(request.getUserDto());
        UserDto newUser = storage.createUser(request).getUserDto();
        log.debug("добавлен новый пользователь, id пользователя: {}", newUser.getId());
        return Optional.of(newUser);
    }

    public void deleteUser(final int userID) {
        storage.deleteUser(userID);
        log.debug("пользователь удален, id пользователя: id {} ", userID);
    }

    public Optional<UserDto> updateUser(RequestUser request) {
        Validate.validateUser(request.getUserDto());
        if (storage.getUserById(request.getId()) == null) {
            throw new NotFoundUserException();
        }
        ResponseUser response = storage.updateUser(request);
        log.debug("данные пользователя обновлены, id пользователя: {}", request.getId());
        return Optional.of(response.getUserDto());
    }

    public Optional<List<UserDto>> getAllUsers() {
        log.debug("возвращен список пользователей");
        return Optional.of(storage.getAllUsers().stream().map(ResponseUser::getUserDto).toList());
    }

    public Optional<UserDto> getUserByID(final int userID) {
        return Optional.of(storage.getUserById(userID).getUserDto());
    }

    public Optional<UserDto> addFriend(final int newFriendID, final int userID) {
        if (!storage.getListFriends(userID).contains(newFriendID)) {
            storage.addFriend(userID, newFriendID);
            log.debug("список друзей пользователя {} : {}", userID, storage.getListFriends(userID));
            return Optional.of(storage.getUserById(newFriendID).getUserDto());
        }
        return Optional.empty();
    }

    public void deleteFriend(final int friendID, final int userID) {
        if (storage.getUserById(friendID) == null || storage.getUserById(userID) == null) {
            throw new NotFoundUserException();
        }
        storage.deleteFriend(userID, friendID);
    }

    public Optional<List<UserDto>> getFriendsUser(final int userID) {
        log.debug("запрос на получение списка друзей пользователя {}", userID);
        ResponseUser response = storage.getUserById(userID);
        if (response == null) {
            throw new NotFoundUserException();
        }
        return Optional.of(storage.getListFriends(userID).stream()
                .map(storage::getUserById)
                .map(ResponseUser::getUserDto)
                .toList());
    }

    public Optional<List<UserDto>> getCommonFriends(final int friendID, final int userID) {
        final List<Integer> friendListUserOne = storage.getListFriends(friendID);
        final List<Integer> friendListUserTwo = storage.getListFriends(userID);
        return Optional.of(friendListUserOne.stream()
                .filter(friendListUserTwo::contains)
                .map(storage::getUserById)
                .map(ResponseUser::getUserDto)
                .toList());
    }
}

package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.RequestUser;
import ru.yandex.practicum.filmorate.storage.database.response.ResponseUser;

import java.util.List;

public interface UserStorage {
    ResponseUser createUser(RequestUser request);

    ResponseUser updateUser(RequestUser request);

    void deleteUser(int userID);

    ResponseUser getUserById(int userID);

    List<ResponseUser> getAllUsers();

    void addFriend(int userID, int friendID);

    void deleteFriend(int userID, int friendID);

    List<Integer> getListFriends(Integer userid);
}

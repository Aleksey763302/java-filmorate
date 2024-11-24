package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {
    void createUser(User user, boolean isCreate);

    void deleteUser(int userId, boolean isDeleteFriends);

    User getUserById(int userId);

    Collection<User> getAllUsers();

    void updateFriendList(Integer user, List<Integer> friends);

    List<Integer> getListFriends(Integer userid);
}

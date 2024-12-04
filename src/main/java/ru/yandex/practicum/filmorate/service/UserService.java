package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage storage;

    public User createUser(final User user) {
        Validate.validateUser(user);
        storage.createUser(user, true);
        log.info("добавлен новый пользователь, id пользователя: {}", user.getId());
        return storage.getUserById(user.getId());
    }

    public void deleteUser(final int userId) {
        storage.deleteUser(userId, true);
        log.info("пользователь удален, id пользователя: id {} ", userId);
    }

    public User updateUser(final User user) {
        Validate.validateUser(user);
        storage.getUserById(user.getId());
        storage.deleteUser(user.getId(), false);
        storage.createUser(user, false);
        log.info("обновление данных пользователя, id пользователя: {}", user.getId());
        return storage.getUserById(user.getId());
    }

    public Collection<User> getAllUsers() {
        log.info("возвращен список пользователей");
        return storage.getAllUsers();
    }

    public Optional<User> addFriend(final int newFriendId, final int userId) {
        log.debug("запуск процесса добавления в друзья, id пользователя: {} id друга {}", userId, newFriendId);

        List<Integer> friendsList = storage.getListFriends(userId);
        log.debug("получен список друзей пользователя {} их количество {}", userId, friendsList.size());
        if (!friendsList.contains(newFriendId)) {
            friendsList.add(newFriendId);
            storage.updateFriendList(userId, friendsList);

            loggingFriends(userId);

            List<Integer> friendsUser = storage.getListFriends(newFriendId);
            if (!friendsUser.contains(userId)) {
                friendsUser.add(userId);
                storage.updateFriendList(newFriendId, friendsUser);

                loggingFriends(newFriendId);
            }
            return Optional.of(storage.getUserById(newFriendId));
        }
        return Optional.empty();
    }

    public void deleteFriend(final Integer friendId, final Integer userId) {

        List<Integer> friendsList = storage.getListFriends(userId);
        friendsList.remove(friendId);
        storage.updateFriendList(userId, friendsList);

        List<Integer> friendsUser = storage.getListFriends(friendId);
        friendsUser.remove(userId);
        storage.updateFriendList(friendId, friendsUser);

    }

    public List<User> getFriendsUser(final int userid) {
        log.info("запрос на получение списка друзей пользователя {}", userid);
        return storage.getListFriends(userid).stream().map(storage::getUserById).toList();
    }

    public List<User> getCommonFriends(final int friendId, final int userid) {
        final List<Integer> friendListUserOne = storage.getListFriends(friendId);
        final List<Integer> friendListUserTwo = storage.getListFriends(userid);
        return friendListUserOne.stream().filter(friendListUserTwo::contains).map(storage::getUserById).toList();
    }


    private void loggingFriends(final Integer userid) {
        log.debug("список друзей пользователя {} : {}", userid, storage.getListFriends(userid));
    }
}

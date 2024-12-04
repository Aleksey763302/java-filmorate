package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundUserException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private final Map<Integer, List<Integer>> friendsUsers = new HashMap<>();

    @Override
    public void createUser(final User user, final boolean isCreate) {
        if (isCreate) {
            user.setId(getNextId());
        }
        users.put(user.getId(), user);
        friendsUsers.put(user.getId(), new ArrayList<>());
    }

    @Override
    public void deleteUser(final int userId, final boolean isDeleteFriends) {
        users.remove(userId);
        if (isDeleteFriends) {
            friendsUsers.remove(userId);
        }
    }

    @Override
    public User getUserById(final int userId) {
        if (users.containsKey(userId)) {
            return users.get(userId);
        }
        throw new NotFoundUserException();
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public void updateFriendList(final Integer userId, final List<Integer> friends) {
        friendsUsers.put(userId, friends);
    }

    @Override
    public List<Integer> getListFriends(Integer userid) {
        if (!users.containsKey(userid)) {
            throw new NotFoundUserException();
        }
        return friendsUsers.get(userid);
    }


    private int getNextId() {
        int foundMaxId = users.keySet().stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++foundMaxId;
    }
}

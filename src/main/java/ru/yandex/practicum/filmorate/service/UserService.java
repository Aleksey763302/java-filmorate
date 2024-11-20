package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final InMemoryUserStorage userStorage;
    private final Map<User, List<User>> friendsUsers = new HashMap<>();

    public Optional<User> addFriend(final User newFriend, final User user) {
        updateTable();
        if (friendsUsers.containsKey(user)) {
            List<User> friendsList = friendsUsers.get(user);
            if (!friendsList.contains(newFriend)) {
                friendsList.add(newFriend);
                friendsUsers.put(user, friendsList);
                List<User> friendsUser = friendsUsers.get(newFriend);
                if(!friendsUser.contains(user)){
                    friendsUser.add(user);
                    friendsUsers.put(newFriend,friendsUser);
                }
                return Optional.of(user);
            }
            return Optional.empty();
        }
        return Optional.empty();
    }

    public void deleteFriend(final User friend, final User user) {
        updateTable();
        if (friendsUsers.containsKey(user)) {
            List<User> friendsList = friendsUsers.get(user);
            friendsList.remove(friend);
            friendsUsers.put(user, friendsList);
            List<User> friendsUser = friendsUsers.get(friend);
            friendsUser.remove(user);
            friendsUsers.put(friend,friendsUser);
        }
    }

    public Collection<User> getCommonFriends(final User friend, final User user) {
        final List<User> friendList = friendsUsers.get(friend);
        return friendsUsers.get(user).stream().filter(friendList::contains).toList();
    }

    private void updateTable() {
        Collection<User> getList = userStorage.getAllUsers();
        Map<User, List<User>> newUsers = new HashMap<>();
        getList.stream().filter(user -> !friendsUsers.containsKey(user))
                .forEach(user -> newUsers.put(user,new ArrayList<>()));
        friendsUsers.putAll(newUsers);
    }
}

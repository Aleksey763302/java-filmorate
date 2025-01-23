package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.RequestUser;
import ru.yandex.practicum.filmorate.model.dto.UserDto;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Optional<UserDto>> createUser(@RequestBody RequestUser user) {
        return ResponseEntity.ok(service.createUser(user));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Optional<UserDto>> updateUser(@RequestBody RequestUser user) {
        return ResponseEntity.ok(service.updateUser(user));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable int id) {
        service.deleteUser(id);
    }

    @GetMapping
    public Optional<List<UserDto>> getUsers() {
        return service.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<UserDto>> getUserById(@PathVariable int id) {
        return ResponseEntity.ok(service.getUserByID(id));
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<UserDto> addFriend(@PathVariable("id") int userId, @PathVariable int friendId) {
        Optional<UserDto> newFriend = service.addFriend(friendId, userId);
        return newFriend.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") int userId, @PathVariable int friendId) {
        service.deleteFriend(friendId, userId);
    }

    @GetMapping("/{id}/friends")
    public Optional<List<UserDto>> getFriendsUser(@PathVariable int id) {
        return service.getFriendsUser(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<Optional<List<UserDto>>> getCommonFriends(@PathVariable("id") int userId,
                                                          @PathVariable("otherId") int otherUserId) {
        return ResponseEntity.ok(service.getCommonFriends(otherUserId, userId));
    }
}
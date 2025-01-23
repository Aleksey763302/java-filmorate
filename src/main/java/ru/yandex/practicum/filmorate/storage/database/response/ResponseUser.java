package ru.yandex.practicum.filmorate.storage.database.response;

import lombok.Builder;
import ru.yandex.practicum.filmorate.model.dto.UserDto;

import java.time.LocalDate;
import java.util.Objects;

@Builder
public class ResponseUser {
    Integer id;
    String email;
    String login;
    String name;
    LocalDate birthday;

    public UserDto getUserDto() {
        UserDto user = new UserDto();
        if (Objects.nonNull(id)) {
            user.setId(id);
        }
        user.setEmail(email);
        user.setLogin(login);
        user.setName(name);
        user.setBirthday(birthday);
        return user;
    }
}

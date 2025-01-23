package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.dto.UserDto;

import java.time.LocalDate;
import java.util.Objects;

@Component
@Data
@RequiredArgsConstructor
public class RequestUser {
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

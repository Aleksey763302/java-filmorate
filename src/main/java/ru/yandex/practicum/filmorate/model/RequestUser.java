package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.dto.UserDto;

import java.time.LocalDate;

@Component
@Data
public class RequestUser {
    Integer id;
    String email;
    String login;
    String name;
    LocalDate birthday;

    public UserDto getUserDto() {
        UserDto user = new UserDto();
        if (id != null) {
            user.setId(id);
        }
        user.setEmail(email);
        user.setLogin(login);
        user.setName(name);
        user.setBirthday(birthday);
        return user;
    }
}

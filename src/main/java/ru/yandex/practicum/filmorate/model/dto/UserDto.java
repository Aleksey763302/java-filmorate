package ru.yandex.practicum.filmorate.model.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {
    Integer id;
    String email;
    String login;
    String name;
    LocalDate birthday;
}

package ru.yandex.practicum.filmorate.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class UserDto {
    Integer id;
    String email;
    String login;
    String name;
    LocalDate birthday;
}

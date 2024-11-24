package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class User {
    Integer id;
    @NotNull
    String email;
    @NotNull
    @NotBlank
    String login;
    String name;
    LocalDate birthday;
}

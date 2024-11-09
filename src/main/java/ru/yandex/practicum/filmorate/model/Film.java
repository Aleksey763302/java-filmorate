package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;
@Builder
@Data
public class Film {
    Integer id;
    String name;
    String description;
    LocalDate releaseDate;
    Duration duration;
}

package ru.yandex.practicum.filmorate.storage.database.response;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.dto.FilmDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
public class ResponseFilm {
    private Integer id;
    private String name;
    private String description;
    private Integer duration;
    private LocalDate releaseDate;
    private Mpa rating;
    private List<Genre> genres;

    public ResponseFilm() {
        genres = new ArrayList<>();
    }

    public FilmDto getFilmDto() {
        return FilmDto.builder()
                .id(id)
                .name(name)
                .description(description)
                .duration(duration)
                .releaseDate(releaseDate)
                .mpa(rating)
                .genres(genres)
                .build();
    }
}

package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.dto.FilmDto;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Data
@Component
@RequiredArgsConstructor
public class RequestCreateFilm {
    private Integer id;
    private String name;
    private String description;
    private Integer duration;
    private LocalDate releaseDate;
    private ID mpa;
    private List<ID> genres;

    public FilmDto getFilmDto() {
        return FilmDto.builder()
                .name(name)
                .description(description)
                .duration(duration)
                .releaseDate(releaseDate)
                .build();
    }

}

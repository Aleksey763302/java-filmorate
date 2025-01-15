package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.dto.FilmDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
@Component
@RequiredArgsConstructor
public class RequestUpdateFilm {
    private Integer id;
    private String name;
    private String description;
    private Integer duration;
    private LocalDate releaseDate;
    private Integer rate;
    private ID mpa;

    public FilmDto getFilmDto() {
        return FilmDto.builder().name(name).description(description).duration(duration).releaseDate(releaseDate).build();
    }

    public RequestCreateFilm getRequestFilm() {
        RequestCreateFilm requestFilm = new RequestCreateFilm();
        requestFilm.setId(id);
        requestFilm.setName(name);
        requestFilm.setDescription(description);
        requestFilm.setDuration(duration);
        requestFilm.setReleaseDate(releaseDate);
        requestFilm.setMpa(mpa);
        List<ID> genreId = new ArrayList<>();
        ID id = new ID();
        id.setId(rate);
        genreId.add(id);
        requestFilm.setGenres(genreId);
        log.debug("getRequestFilm(): {}", requestFilm);
        return requestFilm;
    }
}

package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.database.GenreDbStorage;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreDbStorage storage;

    public Optional<Genre> getGenreById(int id) {
        Genre genre = storage.getGenre(id);
        Validate.validateGenre(genre);
        return Optional.of(genre);
    }

    public Optional<List<Genre>> getGenres() {
        return Optional.of(storage.getGenres());
    }
}

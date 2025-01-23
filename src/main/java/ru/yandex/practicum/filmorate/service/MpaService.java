package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.database.RatingDbStorage;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final RatingDbStorage storage;

    public Optional<Mpa> getMpaById(int id) {
        Mpa mpa = storage.getMpa(id);
        Validate.validateMpa(mpa);
        return Optional.of(mpa);
    }

    public Optional<List<Mpa>> getAllMpa() {
        return Optional.of(storage.getAllMpa());
    }
}

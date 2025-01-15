package ru.yandex.practicum.filmorate.exceptions;

public class IncorrectGenreID extends RuntimeException {
    public IncorrectGenreID(String message) {
        super(message);
    }
}
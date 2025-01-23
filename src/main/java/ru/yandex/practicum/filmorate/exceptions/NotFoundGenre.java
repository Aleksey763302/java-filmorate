package ru.yandex.practicum.filmorate.exceptions;

public class NotFoundGenre extends RuntimeException {
    public NotFoundGenre(String message) {
        super(message);
    }
}

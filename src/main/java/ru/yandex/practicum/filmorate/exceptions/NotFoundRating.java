package ru.yandex.practicum.filmorate.exceptions;

public class NotFoundRating extends RuntimeException {
    public NotFoundRating(String message) {
        super(message);
    }
}

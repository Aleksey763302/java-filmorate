package ru.yandex.practicum.filmorate.exceptions;

public class NotFoundUserException extends RuntimeException {
    public NotFoundUserException() {
        super("пользователь не найден");
    }

    public NotFoundUserException(String message) {
        super(message);
    }
}

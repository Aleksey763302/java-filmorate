package ru.yandex.practicum.filmorate.exceptions;

public class NotValidParamException extends NumberFormatException {
    public NotValidParamException(String message) {
        super(message);
    }
}

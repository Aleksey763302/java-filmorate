package ru.yandex.practicum.filmorate.exceptions;

public class ErrorAddingData extends RuntimeException {
    public ErrorAddingData(String message) {
        super(message);
    }

    public ErrorAddingData(String message, Throwable e) {
        super(message, e);
    }
}
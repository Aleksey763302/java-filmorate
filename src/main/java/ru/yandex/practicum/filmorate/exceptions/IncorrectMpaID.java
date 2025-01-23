package ru.yandex.practicum.filmorate.exceptions;

public class IncorrectMpaID extends RuntimeException {
    public IncorrectMpaID(String message) {
        super(message);
    }
}

package ru.yandex.practicum.filmorate.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ValidationException extends RuntimeException {
    private String reason;

    public ValidationException(String reason) {
        super();
        this.reason = reason;
    }
}

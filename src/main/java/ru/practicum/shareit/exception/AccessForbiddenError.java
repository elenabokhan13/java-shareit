package ru.practicum.shareit.exception;

public class AccessForbiddenError extends RuntimeException {
    public AccessForbiddenError(String message) {
        super(message);
    }
}

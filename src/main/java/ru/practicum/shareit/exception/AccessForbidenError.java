package ru.practicum.shareit.exception;

public class AccessForbidenError extends RuntimeException {
    public AccessForbidenError (String message) {
        super(message);
    }
}

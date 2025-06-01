package ru.edu.penzgtu.lab.exception;

import lombok.Getter;

@Getter
public class PenzGtuException extends RuntimeException {
    private final ErrorType type;

    public PenzGtuException(ErrorType type, String message) {
        super(message);
        this.type = type;
    }

    public PenzGtuException(ErrorType type, String message, Throwable cause) {
        super(message, cause);
        this.type = type;
    }

    public PenzGtuException(ErrorType type, Throwable cause) {
        super(cause);
        this.type = type;
    }

    public PenzGtuException(ErrorType type) {
        super(type.getTitle());
        this.type = type;
    }
}

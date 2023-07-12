package com.backend.tms.exception.custom;

public class TrainerAlreadyExistException extends RuntimeException {
    public TrainerAlreadyExistException(String message) {
        super(message);
    }
}

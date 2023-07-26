package com.backend.tms.exception.custom;

public class TraineeAlreadyExistsException extends RuntimeException {
    public TraineeAlreadyExistsException(String message) {
        super(message);
    }
}

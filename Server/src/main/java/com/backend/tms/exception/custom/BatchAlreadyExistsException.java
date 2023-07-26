package com.backend.tms.exception.custom;

public class BatchAlreadyExistsException extends RuntimeException {
    public BatchAlreadyExistsException(String message) {
        super(message);
    }
}

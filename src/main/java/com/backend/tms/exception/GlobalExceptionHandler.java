package com.backend.tms.exception;

import com.backend.tms.exception.custom.TraineeAlreadyExistsException;
import com.backend.tms.exception.custom.TrainerAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({
            TraineeAlreadyExistsException.class,
            TrainerAlreadyExistException.class
    })
    public ResponseEntity<Object> handleCustomException(Exception ex) {
        if (ex instanceof TraineeAlreadyExistsException || ex instanceof TrainerAlreadyExistException) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

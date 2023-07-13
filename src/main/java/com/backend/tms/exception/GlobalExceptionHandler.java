package com.backend.tms.exception;

import com.backend.tms.exception.custom.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({
            TraineeAlreadyExistsException.class,
            TrainerAlreadyExistException.class,
            BatchNotFoundException.class,
            BatchAlreadyExistsException.class,
            TraineeNotFoundException.class,
            CourseNotFoundException.class,
            CourseAlreadyExistsException.class
    })
    public ResponseEntity<Object> handleCustomException(Exception ex) {
        if (ex instanceof TraineeAlreadyExistsException ) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } else if(ex instanceof BatchNotFoundException ){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }else if(ex instanceof TrainerAlreadyExistException){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }else if (ex instanceof BatchAlreadyExistsException) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }else if (ex instanceof TrainerNotFoundException){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }else if (ex instanceof TraineeNotFoundException){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }else if (ex instanceof CourseNotFoundException){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }else if (ex instanceof CourseAlreadyExistsException){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }else {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

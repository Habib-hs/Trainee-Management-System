package com.backend.tms.exception.custom;

public class TraineeNotFoundException extends RuntimeException{
    public TraineeNotFoundException (String message){
        super(message);
    }
}

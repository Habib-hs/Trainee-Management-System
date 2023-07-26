package com.backend.tms.exception.custom;

public class TrainerNotFoundException extends RuntimeException{
    public TrainerNotFoundException (String message){
        super(message);
    }
}

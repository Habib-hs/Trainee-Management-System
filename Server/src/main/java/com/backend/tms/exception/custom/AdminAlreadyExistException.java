package com.backend.tms.exception.custom;

public class AdminAlreadyExistException extends RuntimeException{
    public AdminAlreadyExistException(String message){
        super(message);
    }
}

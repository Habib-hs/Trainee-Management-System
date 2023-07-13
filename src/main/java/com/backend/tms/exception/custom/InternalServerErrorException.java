package com.backend.tms.exception.custom;

public class InternalServerErrorException extends RuntimeException{
    public InternalServerErrorException (String message){
        super(message);
    }
}

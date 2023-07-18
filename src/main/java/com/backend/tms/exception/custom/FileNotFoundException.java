package com.backend.tms.exception.custom;

public class FileNotFoundException extends RuntimeException{
    public FileNotFoundException(String message){
        super (message);
    }
}

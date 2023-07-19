package com.backend.tms.exception.custom;

public class ClassroomNotFoundException extends RuntimeException{
    public ClassroomNotFoundException (String message){
        super(message);
    }
}

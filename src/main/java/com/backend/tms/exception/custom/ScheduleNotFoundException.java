package com.backend.tms.exception.custom;

public class ScheduleNotFoundException extends RuntimeException{
    public ScheduleNotFoundException (String message){
        super (message);
    }
}

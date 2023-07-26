package com.backend.tms.exception.custom;

public class NoticeNotFoundException extends RuntimeException{
    public NoticeNotFoundException(String message){
        super(message);
    }
}

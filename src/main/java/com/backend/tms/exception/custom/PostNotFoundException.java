package com.backend.tms.exception.custom;

public class PostNotFoundException extends  RuntimeException{
    public PostNotFoundException (String message){
        super (message);
    }
}

package com.backend.tms.exception.custom;

public class CommentNotFoundException extends RuntimeException{
    public CommentNotFoundException (String message){
        super(message);
    }
}

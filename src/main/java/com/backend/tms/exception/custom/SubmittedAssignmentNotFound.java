package com.backend.tms.exception.custom;

public class SubmittedAssignmentNotFound extends RuntimeException{
    public SubmittedAssignmentNotFound (String message){
        super(message);
    }
}

package com.backend.tms.exception.custom;

public class CourseAlreadyExistsException extends RuntimeException{
   public CourseAlreadyExistsException (String message){
       super(message);
   }
}

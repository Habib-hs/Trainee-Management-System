package com.backend.tms.service;


import com.backend.tms.model.Classroom.AssignmentReqModel;
import org.springframework.http.ResponseEntity;
public interface AssignmentService {
    ResponseEntity<Object> createAssignment(AssignmentReqModel assignmentModel);
}
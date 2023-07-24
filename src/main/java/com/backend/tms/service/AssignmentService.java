package com.backend.tms.service;


import com.backend.tms.model.Classroom.AssignmentReqModel;
import org.springframework.http.ResponseEntity;

public interface AssignmentService {
    ResponseEntity<Object> createAssignment(AssignmentReqModel assignmentModel);
    ResponseEntity<Object> getAssignment(Long assignmentId);
     ResponseEntity<Object> updateAssignment(Long assignmentId, AssignmentReqModel assignmentModel);
    ResponseEntity<Object> downloadAssignmentFile(Long assignmentId);
     ResponseEntity<Object> getAllAssignmentsWithoutSubmittedList();
    ResponseEntity<Object> getAssignmentListByTrainer(Long trainerId);

}
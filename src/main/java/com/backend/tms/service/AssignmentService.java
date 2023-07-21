package com.backend.tms.service;


import com.backend.tms.model.Classroom.AssignmentReqModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface AssignmentService {
        ResponseEntity<Object> createAssignment(AssignmentReqModel assignmentModel);
      ResponseEntity<Object> getAssignment(Long assignmentId);
     ResponseEntity<Object> updateAssignment(Long assignmentId, AssignmentReqModel assignmentModel);

    ResponseEntity<Object> downloadAssignmentFile(Long assignmentId);
    public ResponseEntity<Object> getAllAssignmentsWithoutSubmittedList();

}
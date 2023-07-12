package com.backend.tms.service;

import com.backend.tms.model.Classroom.SubmitAssignmentReqModel;
import org.springframework.http.ResponseEntity;

public interface SubmitAssignmentService {
    ResponseEntity<Object> submitAssignment(Long assignmentId, SubmitAssignmentReqModel submitAssignmentModel);
}

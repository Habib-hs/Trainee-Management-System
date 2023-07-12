package com.backend.tms.controller;

import com.backend.tms.model.Classroom.SubmitAssignmentReqModel;
import com.backend.tms.service.SubmitAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/submit-assignment")
public class SubmitAssignmentController {

    private final SubmitAssignmentService submitAssignmentService;

    @Autowired
    public SubmitAssignmentController(SubmitAssignmentService submitAssignmentService) {
        this.submitAssignmentService = submitAssignmentService;
    }

    @PostMapping("/{assignmentId}")
    public ResponseEntity<Object> submitAssignment(@PathVariable("assignmentId") Long assignmentId,
                                                   @RequestBody SubmitAssignmentReqModel submitAssignmentModel) {
        return submitAssignmentService.submitAssignment(assignmentId, submitAssignmentModel);
    }
}

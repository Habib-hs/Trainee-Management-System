package com.backend.tms.controller;

import com.backend.tms.model.Classroom.SubmitAssignmentReqModel;
import com.backend.tms.service.SubmitAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/submit-assignment")
@RequiredArgsConstructor
public class SubmitAssignmentController {

    private final SubmitAssignmentService submitAssignmentService;

    @PostMapping("/{assignmentId}")
    @PreAuthorize("hasRole('TRAINEE')")
    public ResponseEntity<Object> submitAssignment(@PathVariable("assignmentId") Long assignmentId,
                                                   @ModelAttribute SubmitAssignmentReqModel submitAssignmentModel) {
        return submitAssignmentService.submitAssignment(assignmentId, submitAssignmentModel);
    }

    @PutMapping("/{subAssignmentId}")
    @PreAuthorize("hasRole('TRAINEE')")
    public ResponseEntity<Object> updateAssignment(@PathVariable("subAssignmentId") Long subAssignmentId,
                                                   @ModelAttribute SubmitAssignmentReqModel submitAssignmentModel) {
        return submitAssignmentService.updateAssignment(subAssignmentId, submitAssignmentModel);
    }

    @GetMapping("/{subAssignmentId}/download")
    @PreAuthorize("hasRole('TRAINER') or hasRole('TRAINEE')")
    public ResponseEntity<Object> downloadSubAssignment(@PathVariable("subAssignmentId") Long subAssignmentId) {
        return submitAssignmentService.downloadSubAssignment(subAssignmentId);
    }
}

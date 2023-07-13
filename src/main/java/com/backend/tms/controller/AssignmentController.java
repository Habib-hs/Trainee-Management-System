package com.backend.tms.controller;

import com.backend.tms.model.Classroom.AssignmentReqModel;
import com.backend.tms.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/assignment")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    @PostMapping()
    public ResponseEntity<Object> createAssignment(@RequestBody AssignmentReqModel assignmentModel) {
        return assignmentService.createAssignment(assignmentModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getAssignment(@PathVariable("id") Long assignmentId) {
        return assignmentService.getAssignment(assignmentId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateAssignment(@PathVariable("id") Long assignmentId, @RequestBody AssignmentReqModel assignmentModel) {
        return assignmentService.updateAssignment(assignmentId, assignmentModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAssignment(@PathVariable("id") Long assignmentId) {
        return assignmentService.deleteAssignment(assignmentId);
    }

}
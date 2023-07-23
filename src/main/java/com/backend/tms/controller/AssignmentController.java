package com.backend.tms.controller;

import com.backend.tms.model.Classroom.AssignmentReqModel;
import com.backend.tms.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/assignment")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;
    private final ModelMapper modelMapper;

    @PostMapping()
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<Object> createAssignment(@ModelAttribute AssignmentReqModel assignmentModel) {
        return assignmentService.createAssignment(assignmentModel);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('TRAINER') OR hasRole('TRAINEE')")
    public ResponseEntity<Object> getAssignment(@PathVariable("id") Long assignmentId) {
        return assignmentService.getAssignment(assignmentId);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<Object> updateAssignment(@PathVariable("id") Long assignmentId, @ModelAttribute AssignmentReqModel assignmentModel) {
        return assignmentService.updateAssignment(assignmentId, assignmentModel);
    }

    @GetMapping("/{id}/download")
    @PreAuthorize("hasRole('TRAINER') OR hasRole('TRAINEE')")
    public ResponseEntity<Object> downloadAssignmentFile(@PathVariable("id") Long assignmentId) {
         return assignmentService.downloadAssignmentFile(assignmentId);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('TRAINER') OR hasRole('TRAINEE')")
    public ResponseEntity<Object> getAllAssignmentsWithoutSubmittedList() {
     return assignmentService.getAllAssignmentsWithoutSubmittedList();
    }

    @GetMapping("/trainer/{trainerId}")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<Object> getAssignmentListByTrainer(@PathVariable("trainerId") Long trainerId) {
        return assignmentService.getAssignmentListByTrainer(trainerId);
    }

}
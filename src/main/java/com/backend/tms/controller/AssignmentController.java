package com.backend.tms.controller;

import com.backend.tms.entity.AssignmentEntity;
import com.backend.tms.model.Classroom.AssignmentReqModel;
import com.backend.tms.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/assignment")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;
    private final ModelMapper modelMapper;

    @PostMapping()
    public ResponseEntity<Object> createAssignment(@ModelAttribute AssignmentReqModel assignmentModel) {
        return assignmentService.createAssignment(assignmentModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getAssignment(@PathVariable("id") Long assignmentId) {
        return assignmentService.getAssignment(assignmentId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateAssignment(@PathVariable("id") Long assignmentId, @ModelAttribute AssignmentReqModel assignmentModel) {
        return assignmentService.updateAssignment(assignmentId, assignmentModel);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Object> downloadAssignmentFile(@PathVariable("id") Long assignmentId) {
         return assignmentService.downloadAssignmentFile(assignmentId);
    }

}
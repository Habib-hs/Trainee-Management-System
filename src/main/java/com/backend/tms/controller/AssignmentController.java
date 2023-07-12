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

}
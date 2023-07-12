package com.backend.tms.controller;

import com.backend.tms.service.AssignTraineeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assign-trainee")
@RequiredArgsConstructor
public class AssignTraineeController {

    private final AssignTraineeService assignTraineeService;
    @PostMapping("/{batchId}")
    public ResponseEntity<Object> addTraineesToBatch(@PathVariable Long batchId, @RequestBody List<Long> traineeIds) {
           return assignTraineeService.addTraineesToBatch(batchId, traineeIds);
    }

}
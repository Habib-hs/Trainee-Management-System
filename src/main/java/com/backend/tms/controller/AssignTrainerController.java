package com.backend.tms.controller;

import com.backend.tms.service.AssignTrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assign_trainer")
@RequiredArgsConstructor
public class AssignTrainerController {

    private final AssignTrainerService assignTrainerService;
    @PostMapping("/{batchId}")
    public ResponseEntity<Object> addTrainersToBatch(@PathVariable Long batchId, @RequestBody List<Long> trainerIds) {
        return assignTrainerService.addTrainerToBatch(batchId, trainerIds);
    }
}
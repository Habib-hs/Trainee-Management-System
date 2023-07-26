package com.backend.tms.controller;

import com.backend.tms.model.Trainee.TraineeUpdateReqModel;
import com.backend.tms.service.TraineeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainee")
@RequiredArgsConstructor
public class TraineeController {
    private final TraineeService traineeService;

    @GetMapping("/get/all")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TRAINER')")
    public ResponseEntity<Object> getAllTrainees() {
        return traineeService.getAllTrainees();
    }

    @GetMapping("/get/unassigned-trainee")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getAllUnAssignedTrainee() {return traineeService.getAllUnAssignedTrainee();}

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TRAINER') or hasRole('TRAINEE')")
    public ResponseEntity<Object> getTraineeById(@PathVariable("id") Long traineeId) {
        return traineeService.getTraineeById(traineeId);
    }

    @GetMapping("/classroom/{id}")
    @PreAuthorize("hasRole('TRAINEE') or hasRole('ADMIN') or hasRole('TRAINER')")
    public ResponseEntity<Object> getBatchByTraineeId(@PathVariable("id") Long traineeId) {
        return traineeService. getBatchByTraineeId(traineeId);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('TRAINEE')")
    public ResponseEntity<Object> updateTrainee(
            @PathVariable("id") Long traineeId,
            @ModelAttribute TraineeUpdateReqModel traineeModel
    ) {
        return traineeService.updateTrainee(traineeId, traineeModel);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteTrainee(@PathVariable("id") Long traineeId) {
        return traineeService.deleteTrainee(traineeId);
    }
}

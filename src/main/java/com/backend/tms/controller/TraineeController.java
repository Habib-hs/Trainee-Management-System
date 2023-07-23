package com.backend.tms.controller;

import com.backend.tms.model.Trainee.TraineeUpdateReqModel;
import com.backend.tms.service.TraineeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainee")
@RequiredArgsConstructor
public class TraineeController {
    private final TraineeService traineeService;

    @GetMapping("/get/all")
    public ResponseEntity<Object> getAllTrainees() {
        return traineeService.getAllTrainees();
    }

    @GetMapping("/gets/all")
    public ResponseEntity<Object> getAllTrainee() {
        return traineeService.getAllTrainee();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTraineeById(@PathVariable("id") Long traineeId) {
        return traineeService.getTraineeById(traineeId);
    }

    @GetMapping("/classroom/{id}")
    public ResponseEntity<Object> getBatchByTraineeId(@PathVariable("id") Long traineeId) {
        return traineeService. getBatchByTraineeId(traineeId);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateTrainee(
            @PathVariable("id") Long traineeId,
            @ModelAttribute TraineeUpdateReqModel traineeModel
    ) {
        return traineeService.updateTrainee(traineeId, traineeModel);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Object> deleteTrainee(@PathVariable("id") Long traineeId) {
        return traineeService.deleteTrainee(traineeId);
    }
}

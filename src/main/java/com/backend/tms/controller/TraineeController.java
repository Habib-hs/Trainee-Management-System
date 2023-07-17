package com.backend.tms.controller;

import com.backend.tms.model.Trainee.TraineeReqModel;
import com.backend.tms.service.TraineeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainer")
@RequiredArgsConstructor
public class TraineeController {
    private final TraineeService traineeService;

    @GetMapping("/all")
    public ResponseEntity<Object> getAllTrainees() {
        return traineeService.getAllTrainees();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTraineeById(@PathVariable("id") Long traineeId) {
        return traineeService.getTraineeById(traineeId);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateTrainee(
            @PathVariable("id") Long traineeId,
            @RequestBody TraineeReqModel traineeModel
    ) {
        return traineeService.updateTrainee(traineeId, traineeModel);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Object> deleteTrainee(@PathVariable("id") Long traineeId) {
        return traineeService.deleteTrainee(traineeId);
    }
}

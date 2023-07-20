package com.backend.tms.controller;

import com.backend.tms.model.Trainee.TraineeReqModel;
import com.backend.tms.model.Trainer.TrainerUpdateReqModel;
import com.backend.tms.service.TraineeService;
import com.backend.tms.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/trainer")
@RequiredArgsConstructor
public class TrainerController {
    private final TrainerService trainerService;

    @GetMapping("/all")
    public ResponseEntity<Object> getAllTrainers() {
        return trainerService.getAllTrainers();
    }

    @GetMapping("/get/all")
    public ResponseEntity<Object> getAllTrainer() {
        return trainerService.getAllTrainer();
    }

    @GetMapping("/get/allName")
    public ResponseEntity<Object> getTrainerIdAndName(){
        return trainerService.getTrainerIdAndName();
    };

    @GetMapping("/classroom/{id}")
    public ResponseEntity<Object> getBatchByTrainerId(@PathVariable("id") Long trainerId) {
        return trainerService. getBatchByTrainerId(trainerId);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> getTrainerById(@PathVariable("id") Long trainerId) {
        return trainerService.getTrainerById(trainerId);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateTrainer(
            @PathVariable("id") Long trainerId,
            @RequestBody TrainerUpdateReqModel trainerModel
    ) {
        return trainerService.updateTrainer(trainerId, trainerModel);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Object> deleteTrainer(@PathVariable("id") Long trainerId) {
        return trainerService.deleteTrainer(trainerId);
    }
}

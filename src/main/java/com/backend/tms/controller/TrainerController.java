package com.backend.tms.controller;

import com.backend.tms.model.Trainer.TrainerUpdateReqModel;
import com.backend.tms.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trainer")
@RequiredArgsConstructor
public class TrainerController {
    private final TrainerService trainerService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN') ")
    public ResponseEntity<Object> getAllTrainers() {
        return trainerService.getAllTrainers();
    }


    @GetMapping("/classroom/{id}")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<Object> getBatchByTrainerId(@PathVariable("id") Long trainerId) {
        return trainerService. getBatchByTrainerId(trainerId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<Object> getTrainerById(@PathVariable("id") Long trainerId) {
        return trainerService.getTrainerById(trainerId);
    }

    @GetMapping("/get/allName")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getTrainerIdAndName(){
        return trainerService.getTrainerIdAndName();
    };

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('TRAINER'))")
    public ResponseEntity<Object> updateTrainer(
            @PathVariable("id") Long trainerId,
            @ModelAttribute TrainerUpdateReqModel trainerModel
    ) {
        return trainerService.updateTrainer(trainerId, trainerModel);
    }

    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteTrainer(@PathVariable("id") Long trainerId) {
        return trainerService.deleteTrainer(trainerId);
    }
}

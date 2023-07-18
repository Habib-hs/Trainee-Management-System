package com.backend.tms.service;


import com.backend.tms.model.Trainee.TraineeReqModel;
import com.backend.tms.model.Trainer.TrainerUpdateReqModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TrainerService {
    ResponseEntity<Object> getAllTrainers();
    ResponseEntity<Object> getAllTrainer();
    ResponseEntity<Object> getTrainerById(Long trainerId);
    ResponseEntity<Object> updateTrainer(Long trainerId, TrainerUpdateReqModel trainerModel);
    ResponseEntity<Object> deleteTrainer(Long trainerId);

    ResponseEntity<Object> getTrainerIdAndName();
}

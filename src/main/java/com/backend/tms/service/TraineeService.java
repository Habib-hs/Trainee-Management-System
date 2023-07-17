package com.backend.tms.service;

import com.backend.tms.model.Trainee.TraineeReqModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TraineeService {
    ResponseEntity<Object> getAllTrainees();
    ResponseEntity<Object> getTraineeById(Long traineeId);
    ResponseEntity<Object> updateTrainee(Long traineeId, TraineeReqModel traineeModel);
    ResponseEntity<Object> deleteTrainee(Long traineeId);
}

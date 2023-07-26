package com.backend.tms.service;

import com.backend.tms.model.Trainer.AddTrainerReqModel;
import org.springframework.http.ResponseEntity;

public interface AssignTrainerService {
     ResponseEntity<Object> addTrainerToBatch(AddTrainerReqModel requestModel);
}

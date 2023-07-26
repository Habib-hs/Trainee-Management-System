package com.backend.tms.service;

import com.backend.tms.model.Trainee.AddTraineeReqModel;
import org.springframework.http.ResponseEntity;


public interface AssignTraineeService {
    ResponseEntity<Object> addTraineesToBatch(AddTraineeReqModel requestModel);
}

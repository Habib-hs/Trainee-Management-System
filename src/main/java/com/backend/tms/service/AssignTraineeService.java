package com.backend.tms.service;

import com.backend.tms.model.Trainee.AddTraineeReqModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface AssignTraineeService {
    ResponseEntity<Object> addTraineesToBatch(AddTraineeReqModel requestModel);
}

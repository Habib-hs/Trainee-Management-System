package com.backend.tms.service;


import com.backend.tms.model.Admin.AdminReqModel;
import com.backend.tms.model.Common.AuthenticationReqModel;
import com.backend.tms.model.Trainee.TraineeReqModel;
import com.backend.tms.model.Trainer.TrainerReqModel;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<Object> login(AuthenticationReqModel requestModel);
    ResponseEntity<Object> registerAdmin(AdminReqModel adminModel);
    ResponseEntity<Object> registerTrainee(TraineeReqModel traineeModel);
    ResponseEntity<Object> registerTrainer(TrainerReqModel trainerModel);
}
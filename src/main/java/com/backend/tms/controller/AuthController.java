package com.backend.tms.controller;

import com.backend.tms.model.Admin.AdminReqModel;
import com.backend.tms.model.Common.AuthenticationReqModel;
import com.backend.tms.model.Trainee.TraineeReqModel;
import com.backend.tms.model.Trainer.TrainerReqModel;
import com.backend.tms.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService ;

    @PostMapping("/login")
    public ResponseEntity<Object> register(@RequestBody AuthenticationReqModel requestModel){
        return authService.login(requestModel);
    }

    @PostMapping("/admin/register")
    public ResponseEntity<Object> createAdmin(@RequestBody AdminReqModel adminModel) {
        return authService.registerAdmin(adminModel);
    }


    @PostMapping("/trainee/register")
    public ResponseEntity<Object> createTrainee(@RequestBody TraineeReqModel traineeModel) {
        return authService.registerTrainee(traineeModel);
    }

    @PostMapping("/trainer/register")
    public ResponseEntity<Object> createTrainer(@RequestBody TrainerReqModel trainerModel) {
        return authService.registerTrainer(trainerModel);
    }


}

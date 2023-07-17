package com.backend.tms.controller;

import com.backend.tms.model.Trainee.AddTrainerReqModel;
import com.backend.tms.service.AssignTrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assign_trainer")
@RequiredArgsConstructor
public class AssignTrainerController {

    private final AssignTrainerService assignTrainerService;
    @PostMapping()
    public ResponseEntity<Object> addTrainersToBatch(@RequestBody AddTrainerReqModel requestModel) {
        return assignTrainerService.addTrainerToBatch(requestModel);
    }
}
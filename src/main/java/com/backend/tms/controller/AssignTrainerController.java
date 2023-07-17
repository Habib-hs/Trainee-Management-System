package com.backend.tms.controller;

import com.backend.tms.model.Trainer.AddTrainerReqModel;
import com.backend.tms.service.AssignTrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
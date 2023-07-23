package com.backend.tms.controller;

import com.backend.tms.model.Trainee.AddTraineeReqModel;
import com.backend.tms.service.AssignTraineeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assign-trainee")
@RequiredArgsConstructor
public class AssignTraineeController {

    private final AssignTraineeService assignTraineeService;

    @PostMapping()
    @PreAuthorize(" hasRole('ADMIN')")
    public ResponseEntity<Object> addTraineesToBatch(@RequestBody AddTraineeReqModel requestModel) {
        return assignTraineeService.addTraineesToBatch(requestModel);
    }

}
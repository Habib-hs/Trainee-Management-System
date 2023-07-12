package com.backend.tms.controller;


import com.backend.tms.model.ScheduleBatch.ScheduleBatchReqModel;
import com.backend.tms.service.ScheduleBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedule-batch")
@RequiredArgsConstructor
public class BatchSchedulingController {

    private final ScheduleBatchService scheduleBatchService;
    @PostMapping()
    public ResponseEntity<Object> createScheduleBatch(@RequestBody ScheduleBatchReqModel scheduleBatchModel) {
        return scheduleBatchService.createScheduleBatch(scheduleBatchModel);
    }
}
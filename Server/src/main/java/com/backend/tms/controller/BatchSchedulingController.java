package com.backend.tms.controller;

import com.backend.tms.model.ScheduleBatch.ScheduleBatchReqModel;
import com.backend.tms.service.ScheduleBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schedule-batch")
@RequiredArgsConstructor
public class BatchSchedulingController {

    private final ScheduleBatchService scheduleBatchService;
    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> createScheduleBatch(@RequestBody ScheduleBatchReqModel scheduleBatchModel) {
        return scheduleBatchService.createScheduleBatch(scheduleBatchModel);
    }
    @GetMapping()
    @PreAuthorize("hasRole('ADMIN') or hasRole('TRAINER')")
    public ResponseEntity<Object> getScheduleNames(){
        return scheduleBatchService.getScheduleNames();
    }

    @PutMapping("update/{scheduleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> updateScheduleBatch(
            @PathVariable Long scheduleId,
            @RequestBody ScheduleBatchReqModel scheduleBatchModel) {
        return scheduleBatchService.updateScheduleBatch(scheduleId, scheduleBatchModel);
    }

    @DeleteMapping("/delete/{scheduleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteScheduleBatch(@PathVariable Long scheduleId) {
        return scheduleBatchService.deleteScheduleBatch(scheduleId);
    }


}
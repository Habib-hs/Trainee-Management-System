package com.backend.tms.service;


import com.backend.tms.model.ScheduleBatch.ScheduleBatchReqModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface ScheduleBatchService {
    ResponseEntity<Object> createScheduleBatch( ScheduleBatchReqModel scheduleBatchModel);
    ResponseEntity<Object> getScheduleNames();
    ResponseEntity<Object> updateScheduleBatch(Long scheduleId, ScheduleBatchReqModel scheduleBatchModel);
     ResponseEntity<Object> deleteScheduleBatch(Long scheduleId);
}
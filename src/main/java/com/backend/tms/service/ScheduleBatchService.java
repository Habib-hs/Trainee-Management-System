package com.backend.tms.service;


import com.backend.tms.model.ScheduleBatch.ScheduleBatchReqModel;
import org.springframework.http.ResponseEntity;

public interface ScheduleBatchService {
    public ResponseEntity<Object> createScheduleBatch( ScheduleBatchReqModel scheduleBatchModel);
    ResponseEntity<Object> getScheduleNames();

}
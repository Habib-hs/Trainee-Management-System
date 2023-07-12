package com.backend.tms.service;


import com.backend.tms.model.ScheduleBatch.ScheduleBatchReqModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface ScheduleBatchService {
    public ResponseEntity<Object> createScheduleBatch( ScheduleBatchReqModel scheduleBatchModel);
}
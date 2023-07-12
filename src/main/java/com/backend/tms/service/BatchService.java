package com.backend.tms.service;

import com.backend.tms.model.Batch.BatchReqModel;
import org.springframework.http.ResponseEntity;

public interface BatchService {
    ResponseEntity<Object> createBatch(BatchReqModel batchModel);
    ResponseEntity<Object> deleteBatch(Long batchId);
}

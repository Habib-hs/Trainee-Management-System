package com.backend.tms.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface AssignTraineeService {
    ResponseEntity<Object> addTraineesToBatch(Long batchId, List<Long> traineeIds);
}

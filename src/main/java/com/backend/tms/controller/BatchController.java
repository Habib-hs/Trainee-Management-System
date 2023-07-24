package com.backend.tms.controller;

import com.backend.tms.model.Batch.BatchReqModel;
import com.backend.tms.service.BatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/batch")
@RequiredArgsConstructor
public class BatchController {
    private final BatchService batchService;
    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> createBatch(@RequestBody BatchReqModel batchModel) {
        return batchService.createBatch(batchModel);
    }
    @GetMapping("/get/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getAllBatches() {
        return batchService.getAllBatches();
    }

    @GetMapping("/get/allName")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getAllBatchName() {
        return batchService.getAllBatchName();
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getBatch(@PathVariable("id") Long batchId) {
        return batchService.getBatch(batchId);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> updateBatch(@PathVariable("id") Long batchId, @RequestBody BatchReqModel batchModel) {
        return batchService.updateBatch(batchId, batchModel);
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteBatch(@PathVariable("id") Long batchId) {
        return batchService.deleteBatch(batchId);
    }
}
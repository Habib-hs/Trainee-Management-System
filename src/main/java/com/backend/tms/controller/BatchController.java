package com.backend.tms.controller;


import com.backend.tms.model.Batch.BatchReqModel;
import com.backend.tms.service.BatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/batch")
@RequiredArgsConstructor
public class BatchController {
    private final BatchService batchService;
    @PostMapping()
    public ResponseEntity<Object> createBatch(@RequestBody BatchReqModel batchModel) {
        return batchService.createBatch(batchModel);
    }

    @GetMapping("/get/all")
    public ResponseEntity<Object> getAllBatches() {
        return batchService.getAllBatches();
    }

    @GetMapping("/get/allName")
    public ResponseEntity<Object> getAllBatchName() {
        return batchService.getAllBatchName();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getBatch(@PathVariable("id") Long batchId) {
        return batchService.getBatch(batchId);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateBatch(@PathVariable("id") Long batchId, @RequestBody BatchReqModel batchModel) {
        return batchService.updateBatch(batchId, batchModel);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteBatch(@PathVariable("id") Long batchId) {
        return batchService.deleteBatch(batchId);
    }
}
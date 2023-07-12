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

    @PostMapping("/create")
    public ResponseEntity<Object> createBatch(@RequestBody BatchReqModel batchModel) {
        return batchService.createBatch(batchModel);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object>deleteBatch(@PathVariable("id") Long batchId) {
        return batchService.deleteBatch(batchId);
    }

}
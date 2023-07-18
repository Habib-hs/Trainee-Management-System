package com.backend.tms.service.Impl;

import com.backend.tms.entity.BatchEntity;
import com.backend.tms.exception.custom.BatchNotFoundException;
import com.backend.tms.model.Batch.BatchReqModel;
import com.backend.tms.model.Batch.BatchResModel;
import com.backend.tms.repository.BatchRepository;
import com.backend.tms.service.BatchService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BatchServiceImp implements BatchService {
    private final BatchRepository batchRepository;
    private final ModelMapper modelMapper;
    @Override
    public ResponseEntity<Object> createBatch(BatchReqModel batchModel) {
        // Check if a batch with the same name already exists
        String batchName = batchModel.getBatchName();
        BatchEntity existingBatch = batchRepository.findByBatchName(batchName);
        if (existingBatch != null) {
          // throw new BatchAlreadyExistsException("Batch Already exist with the same name!");
            return new ResponseEntity<>("Batch already exist", HttpStatus.BAD_REQUEST);
        }

        // Create a new BatchEntity
        BatchEntity batchEntity = modelMapper.map(batchModel,BatchEntity.class);
        batchRepository.save(batchEntity);

        // If the save operation is successful, return a success message
        return new ResponseEntity<>("Batch added successfully", HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Object> getBatch(Long batchId) {
        // Check if the batch exists
        BatchEntity batchEntity = batchRepository.findById(batchId)
                .orElseThrow(() -> new BatchNotFoundException("Batch not found"));
        BatchResModel batchModel = modelMapper.map(batchEntity, BatchResModel.class);
        return new ResponseEntity<>(batchModel, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getAllBatches() {
        List<BatchEntity> batchEntities = batchRepository.findAll();
        // Create a response object
        Map<String, Object> response = new HashMap<>();
        response.put("Total Batch", batchEntities.size());
        response.put("Batches", batchEntities);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getAllBatchName() {
        List<BatchEntity> batchEntities = batchRepository.findAll();
        // Create a response object
        List<Map<String, Object>> batches = new ArrayList<>();
        for (BatchEntity batch : batchEntities) {
            Map<String, Object>  batchData = new HashMap<>();
            batchData.put("id", batch.getId());
            batchData.put("name", batch.getBatchName());
            batches.add(batchData);
        }
        // Create the final response
        Map<String, Object> response = new HashMap<>();
        response.put("Total Batches", batches.size());
        response.put("Batches", batches);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> updateBatch(Long batchId, BatchReqModel batchModel) {
        // Check if the batch exists
        BatchEntity batchEntity = batchRepository.findById(batchId)
                .orElseThrow(() -> new BatchNotFoundException("Batch not found"));
        // Update the batch details
        batchEntity.setBatchName(batchModel.getBatchName());
        batchEntity.setStartDate(batchModel.getStartDate());
        batchEntity.setEndDate(batchModel.getEndDate());
        // Save the updated batch entity
        batchRepository.save(batchEntity);
        // Return a success message
        return new ResponseEntity<>("Batch updated successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> deleteBatch(Long batchId) {
        // Check if the batch exists, if exist then delete
        batchRepository.findById(batchId).orElseThrow(()->new BatchNotFoundException("Batch not found"));
        batchRepository.deleteById(batchId);
        return new ResponseEntity<>("Batch deleted successfully", HttpStatus.OK);
    }

}

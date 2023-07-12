package com.backend.tms.service.Impl;

import com.backend.tms.entity.BatchEntity;
import com.backend.tms.entity.ClassroomEntity;
import com.backend.tms.exception.custom.BatchAlreadyExistsException;
import com.backend.tms.exception.custom.BatchNotFoundException;
import com.backend.tms.model.Batch.BatchReqModel;
import com.backend.tms.repository.BatchRepository;
import com.backend.tms.repository.ClassroomRepository;
import com.backend.tms.service.BatchService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BatchServiceImp implements BatchService {
    private final BatchRepository batchRepository;
    private final ClassroomRepository classroomRepository;
    private final ModelMapper modelMapper;
    @Override
    public ResponseEntity<Object> createBatch(BatchReqModel batchModel) {
        // Check if a batch with the same name already exists
        String batchName = batchModel.getBatchName();
        BatchEntity existingBatch = batchRepository.findByBatchName(batchName);
        if (existingBatch != null) {
           throw new BatchAlreadyExistsException("Batch Already exist with the same name!");
        }


        // Create a new BatchEntity
        BatchEntity batchEntity = modelMapper.map(batchModel, BatchEntity.class);
        batchRepository.save(batchEntity);

        // Add classroom for the batch
        ClassroomEntity classroomEntity = ClassroomEntity.builder()
                .id(batchEntity.getId())
                .className(batchEntity.getBatchName())
                .build();
        classroomRepository.save(classroomEntity);

        // If the save operation is successful, return a success message
        return new ResponseEntity<>("Batch added successfully", HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Object> deleteBatch(Long batchId) {
        // Check if the batch exists
        batchRepository.findById(batchId).orElseThrow(()->new BatchNotFoundException("Batch not found"));
        // Delete the batch
        batchRepository.deleteById(batchId);
        // Delete the associated classroom
        classroomRepository.deleteById(batchId);
        // Return a success message
        return new ResponseEntity<>("Batch deleted successfully", HttpStatus.OK);
    }

}

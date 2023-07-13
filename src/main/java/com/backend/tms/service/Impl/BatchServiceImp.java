package com.backend.tms.service.Impl;

import com.backend.tms.entity.BatchEntity;
import com.backend.tms.entity.ClassroomEntity;
import com.backend.tms.exception.custom.BatchAlreadyExistsException;
import com.backend.tms.exception.custom.BatchNotFoundException;
import com.backend.tms.model.Batch.BatchReqModel;
import com.backend.tms.model.Batch.BatchResModel;
import com.backend.tms.repository.BatchRepository;
import com.backend.tms.repository.ClassroomRepository;
import com.backend.tms.service.BatchService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        BatchEntity batchEntity = modelMapper.map(batchModel,BatchEntity.class);

        // Add classroom for the batch
        ClassroomEntity classroomEntity = ClassroomEntity.builder()
                .id(batchEntity.getId())
                .className(batchEntity.getBatchName())
                .build();
        classroomRepository.save(classroomEntity);
        batchEntity.setClassroom(classroomEntity);;
        batchRepository.save(batchEntity);

        // If the save operation is successful, return a success message
        return new ResponseEntity<>("Batch added successfully", HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Object> getBatch(Long batchId) {
        // Check if the batch exists
        BatchEntity batchEntity = batchRepository.findById(batchId)
                .orElseThrow(() -> new BatchNotFoundException("Batch not found"));

        // Map BatchEntity to BatchReqModel
        BatchResModel batchModel = modelMapper.map(batchEntity, BatchResModel.class);

        // Return the BatchReqModel
        return new ResponseEntity<>(batchModel, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getAllBatches() {
        // Retrieve all batches from the repository
        List<BatchEntity> batchEntities = batchRepository.findAll();

        // Create a list to store BatchReqModels
        List<BatchResModel> batchModels = new ArrayList<>();

        // Iterate over each BatchEntity and map it to BatchReqModel
        for (BatchEntity batchEntity : batchEntities) {
            BatchResModel batchModel = modelMapper.map(batchEntity, BatchResModel.class);
            batchModels.add(batchModel);
        }

        // Return the list of BatchReqModels
        return new ResponseEntity<>(batchModels, HttpStatus.OK);
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

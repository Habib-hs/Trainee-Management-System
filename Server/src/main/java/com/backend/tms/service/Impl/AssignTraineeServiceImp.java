package com.backend.tms.service.Impl;

import com.backend.tms.entity.BatchEntity;
import com.backend.tms.entity.TraineeEntity;
import com.backend.tms.exception.custom.BatchNotFoundException;
import com.backend.tms.exception.custom.TraineeNotFoundException;
import com.backend.tms.model.Trainee.AddTraineeReqModel;
import com.backend.tms.repository.BatchRepository;
import com.backend.tms.repository.TraineeRepository;
import com.backend.tms.service.AssignTraineeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AssignTraineeServiceImp implements AssignTraineeService {

    private final BatchRepository batchRepository;
    private final TraineeRepository traineeRepository;

    @Override
    public ResponseEntity<Object> addTraineesToBatch(AddTraineeReqModel requestModel) {
        BatchEntity batch = batchRepository.findById(requestModel.getBatchId()).orElseThrow(() ->
                new BatchNotFoundException("Batch not found"));

        //get all the trainees
        Set<TraineeEntity> trainee = new HashSet<>(traineeRepository.findAllById(requestModel.getTraineeIds()));
        if (trainee.size() == 0) {
            throw new TraineeNotFoundException("No trainee found for the provided IDs");
        }

        //add the trainee to the batch
            batch.getTrainees().addAll(trainee);
            batchRepository.save(batch);

        return new ResponseEntity<>("Assign Trainees successfully", HttpStatus.OK);
    }
}
package com.backend.tms.service.Impl;

import com.backend.tms.entity.BatchEntity;
import com.backend.tms.entity.TraineeEntity;
import com.backend.tms.exception.custom.BatchNotFoundException;
import com.backend.tms.exception.custom.TraineeNotFoundException;
import com.backend.tms.repository.BatchRepository;
import com.backend.tms.repository.TraineeRepository;
import com.backend.tms.service.AssignTraineeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignTraineeServiceImp implements AssignTraineeService {

    private final BatchRepository batchRepository;
    private final TraineeRepository traineeRepository;

    @Override
    public ResponseEntity<Object> addTraineesToBatch(Long batchId, List<Long> traineeIds) {
        BatchEntity batch = batchRepository.findById(batchId).orElseThrow(() -> new BatchNotFoundException("Batch not found"));

        for (Long traineeId : traineeIds) {
            var trainee =traineeRepository.findById(traineeId);
            if(trainee.isPresent()){
                batch.getTrainees().add(trainee.get());
            }else{
                System.out.println("No batch found with id"+ traineeId);
            }
        }
        batchRepository.save(batch);

        // Return a success message
        return new ResponseEntity<>("Assign Trainees successfully", HttpStatus.OK);

    }
}
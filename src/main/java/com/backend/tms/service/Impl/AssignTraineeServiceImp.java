package com.backend.tms.service.Impl;

import com.backend.tms.entity.BatchEntity;
import com.backend.tms.entity.TraineeEntity;
import com.backend.tms.entity.TrainerEntity;
import com.backend.tms.exception.custom.BatchNotFoundException;
import com.backend.tms.exception.custom.TraineeNotFoundException;
import com.backend.tms.exception.custom.TrainerNotFoundException;
import com.backend.tms.repository.BatchRepository;
import com.backend.tms.repository.TraineeRepository;
import com.backend.tms.service.AssignTraineeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AssignTraineeServiceImp implements AssignTraineeService {

    private final BatchRepository batchRepository;
    private final TraineeRepository traineeRepository;

    @Override
    public ResponseEntity<Object> addTraineesToBatch(Long batchId, List<Long> traineeIds) {
        BatchEntity batch = batchRepository.findById(batchId).orElseThrow(() -> new BatchNotFoundException("Batch not found"));


        Set<TraineeEntity> trainee = new HashSet<>(traineeRepository.findAllById(traineeIds));
        if (trainee.size() == 0) {
            throw new TraineeNotFoundException("No trainee found for the provided IDs");
        }

        //need to add here if a trainer exist in same or another batch.
        // the trainee that are not assigned in a batch earlier keep them in List<Long> traineeIds
        //the trainee that are exist another batch keep them in a list name existed trainee


        //all the trainees are not present in db don't save them
        if (traineeIds.size() != trainee.size()) {
            int diff = traineeIds.size() - trainee.size();
            batch.getTrainees().addAll(trainee);
            batchRepository.save(batch);
            String responseMessage = diff + " trainee IDs you provided are not valid. Only valid trainee are added to the Batch";
            return new ResponseEntity<>(responseMessage, HttpStatus.OK);
        }else{
            batch.getTrainees().addAll(trainee);
            batchRepository.save(batch);
        }

        // Return a success message
        return new ResponseEntity<>("Assign Trainees successfully", HttpStatus.OK);

    }
}
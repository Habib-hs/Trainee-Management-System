package com.backend.tms.service.Impl;

import com.backend.tms.entity.BatchEntity;
import com.backend.tms.entity.TrainerEntity;
import com.backend.tms.exception.custom.BatchNotFoundException;
import com.backend.tms.exception.custom.TrainerNotFoundException;
import com.backend.tms.repository.BatchRepository;
import com.backend.tms.repository.TrainerRepository;
import com.backend.tms.service.AssignTrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AssignTrainerServiceImp implements AssignTrainerService {
    private final BatchRepository batchRepository;
    private final TrainerRepository trainerRepository;
    @Override
    public ResponseEntity<Object> addTrainerToBatch(Long batchId, List<Long> trainerIds) {

        //if batch not found
            BatchEntity batchEntity = batchRepository.findById(batchId)
                    .orElseThrow(() -> new BatchNotFoundException("Batch not found"));

        Set<TrainerEntity> trainers = new HashSet<>(trainerRepository.findAllById(trainerIds));
        if (trainers.size() == 0) {
            throw new TrainerNotFoundException("No trainers found for the provided IDs");
        }

        //all the trainers are not present in db
            if (trainerIds.size() != trainers.size()) {
                int diff = trainerIds.size() - trainers.size();
                batchEntity.getTrainers().addAll(trainers);
                batchRepository.save(batchEntity);
                String responseMessage = diff + " trainers IDs you provided are not valid. Only valid trainers are added to the Batch";
                return new ResponseEntity<>(responseMessage, HttpStatus.OK);
            }


            //all the trainers are valid
        batchEntity.getTrainers().addAll(trainers);
        batchRepository.save(batchEntity);

        // Return a success message
        return new ResponseEntity<>("Assign Trainer successfully", HttpStatus.OK);
    }
}

package com.backend.tms.service.Impl;

import com.backend.tms.entity.TraineeEntity;
import com.backend.tms.entity.TrainerEntity;
import com.backend.tms.exception.custom.TrainerNotFoundException;
import com.backend.tms.model.Trainer.TrainerResModel;
import com.backend.tms.model.Trainer.TrainerUpdateReqModel;
import com.backend.tms.repository.BatchRepository;
import com.backend.tms.repository.TrainerRepository;
import com.backend.tms.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainerServiceImp implements TrainerService {
    private final ModelMapper modelMapper;
    private final TrainerRepository trainerRepository;
    private final BatchRepository batchRepository;

    @Override
    public ResponseEntity<Object> updateTrainer(Long trainerId, TrainerUpdateReqModel trainerModel) {
        // Check if the trainer exists
        TrainerEntity trainerEntity = trainerRepository.findById(trainerId)
                .orElseThrow(() -> new TrainerNotFoundException("Trainer not found with ID: " + trainerId));

        //update the trainer Entity
        trainerEntity.setFullName(trainerModel.getFullName());
        trainerEntity.setProfilePicture(trainerModel.getProfilePicture());
        trainerEntity.setDesignation(trainerModel.getDesignation());
        trainerEntity.setJoiningDate(trainerModel.getJoiningDate());
        trainerEntity.setYearsOfExperience(trainerModel.getYearsOfExperience());
        trainerEntity.setExpertises(trainerModel.getExpertises());
        trainerEntity.setContactNumber(trainerModel.getContactNumber());
        trainerEntity.setPresentAddress(trainerModel.getPresentAddress());
        trainerRepository.save(trainerEntity);
        // Return a success message
        return new ResponseEntity<>("Trainee updated successfully", HttpStatus.OK);

    }
    @Override
    public ResponseEntity<Object> getAllTrainers() {
        List<TrainerEntity> trainerEntityList = trainerRepository.findAll();
        //create a response object
        Map<String, Object> response = new HashMap<>();
        response.put("Total Trainer", trainerEntityList.size());
        response.put("Trainer", trainerEntityList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getAllTrainer() {
        List<Long> assignedTrainerIds = batchRepository.findAll().stream()
                .flatMap(batch -> batch.getTrainers().stream())
                .map(TrainerEntity::getId)
                .collect(Collectors.toList());

        List<TrainerEntity> unassignedTrainer = trainerRepository.findAll().stream()
                .filter(trainer -> !assignedTrainerIds.contains(trainer.getId()))
                .collect(Collectors.toList());

        // Create a response object
        Map<String, Object> response = new HashMap<>();
        response.put("Total Trainer", unassignedTrainer.size());
        response.put("Trainer", unassignedTrainer);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Object> getTrainerIdAndName() {
        List<TrainerEntity> trainerEntityList = trainerRepository.findAll();

        // Create a response object
        List<Map<String, Object>> trainers = new ArrayList<>();

        // Iterate over each trainer and extract the name and ID
        for (TrainerEntity trainer : trainerEntityList) {
            Map<String, Object> trainerData = new HashMap<>();
            trainerData.put("id", trainer.getId());
            trainerData.put("name", trainer.getFullName());
            trainers.add(trainerData);
        }

        // Create the final response
        Map<String, Object> response = new HashMap<>();
        response.put("Total Trainer", trainers.size());
        response.put("Trainers", trainers);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getTrainerById(Long trainerId) {
        Optional<TrainerEntity> trainerEntity = trainerRepository.findById(trainerId);
        if(trainerEntity ==null){
            throw new TrainerNotFoundException("Trainer not found with ID: " + trainerId);
        }
        TrainerResModel trainer = modelMapper.map(trainerEntity, TrainerResModel.class);
        return new ResponseEntity<>(trainer, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> deleteTrainer(Long trainerId) {
        // Check if the Trainer exists
        trainerRepository.findById(trainerId).orElseThrow(()->new TrainerNotFoundException("Trainer not found"));
        // Delete the Trainer
        trainerRepository.deleteById(trainerId);
        return new ResponseEntity<>("Trainer deleted successfully", HttpStatus.OK);
    }
}

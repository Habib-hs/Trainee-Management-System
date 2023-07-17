package com.backend.tms.service.Impl;

import com.backend.tms.entity.TrainerEntity;
import com.backend.tms.exception.custom.TrainerNotFoundException;
import com.backend.tms.model.Trainer.TrainerResModel;
import com.backend.tms.model.Trainer.TrainerUpdateReqModel;
import com.backend.tms.repository.TrainerRepository;
import com.backend.tms.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainerServiceImp implements TrainerService {
    private final ModelMapper modelMapper;
    private final TrainerRepository trainerRepository;
    @Override
    public ResponseEntity<Object> getAllTrainers() {
        List<TrainerEntity> trainerEntityList = trainerRepository.findAll();
        //create a response object
        Map<String, Object> response = new HashMap<>();
        response.put("Total Trainer", trainerEntityList.size());
        response.put("Trainees", trainerEntityList);
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
    public ResponseEntity<Object> deleteTrainer(Long trainerId) {
        // Check if the Trainer exists
        trainerRepository.findById(trainerId).orElseThrow(()->new TrainerNotFoundException("Trainer not found"));
        // Delete the Trainer
        trainerRepository.deleteById(trainerId);
        return new ResponseEntity<>("Trainer deleted successfully", HttpStatus.OK);
    }
}

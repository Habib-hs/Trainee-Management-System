package com.backend.tms.service.Impl;

import com.backend.tms.entity.BatchEntity;
import com.backend.tms.entity.TraineeEntity;
import com.backend.tms.entity.TrainerEntity;
import com.backend.tms.entity.UserEntity;
import com.backend.tms.exception.custom.TrainerNotFoundException;
import com.backend.tms.model.Trainer.TrainerResModel;
import com.backend.tms.model.Trainer.TrainerUpdateReqModel;
import com.backend.tms.repository.BatchRepository;
import com.backend.tms.repository.TrainerRepository;
import com.backend.tms.repository.UserRepository;
import com.backend.tms.service.TrainerService;
import com.backend.tms.utlis.AppConstants;
import com.backend.tms.utlis.FileService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainerServiceImp implements TrainerService {
    private final ModelMapper modelMapper;
    private final TrainerRepository trainerRepository;
    private final BatchRepository batchRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<Object> updateTrainer(Long trainerId, TrainerUpdateReqModel trainerModel) {
        // Check if the trainer exists
        TrainerEntity trainerEntity = trainerRepository.findById(trainerId)
                .orElseThrow(() -> new TrainerNotFoundException("Trainer not found with ID: " + trainerId));

        MultipartFile file = trainerModel.getProfilePicture();
        String fileUrl = null;
        if (file != null && !file.isEmpty()) {
            try {
                fileUrl = FileService.uploadImage(file, AppConstants.IMAGE_UPLOAD_DIR);
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
            // Set the profile picture URL only if the image upload was successful
            trainerEntity.setProfilePicture(fileUrl);
        }

        //update the trainer Entity
        trainerEntity.setFullName(trainerModel.getFullName());
        trainerEntity.setDesignation(trainerModel.getDesignation());
        trainerEntity.setJoiningDate(trainerModel.getJoiningDate());
        trainerEntity.setYearsOfExperience(trainerModel.getYearsOfExperience());
        trainerEntity.setExpertises(trainerModel.getExpertises());
        trainerEntity.setContactNumber(trainerModel.getContactNumber());
        trainerEntity.setPresentAddress(trainerModel.getPresentAddress());
        trainerRepository.save(trainerEntity);
        // Return a success message
        return new ResponseEntity<>("Trainer updated successfully", HttpStatus.OK);

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
    public ResponseEntity<Object> getBatchByTrainerId(Long trainerId) {
        TrainerEntity trainer = trainerRepository.findById(trainerId).orElse(null);
        if (trainer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trainer not found");
        }
        Set<BatchEntity> batches = trainer.getBatches(); // Assuming there's a getter for the batches in TrainerEntity
        List<Long> batchIds = batches.stream().map(BatchEntity::getId).collect(Collectors.toList());
        return ResponseEntity.ok(batchIds);
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
      TrainerEntity trainer=  trainerRepository.findById(trainerId).orElseThrow(()->new TrainerNotFoundException("Trainer not found"));
        UserEntity user = trainer.getUser();
        if(user!=null){
            userRepository.delete(user);
        }
        trainerRepository.deleteById(trainerId);
        return new ResponseEntity<>("Trainer deleted successfully", HttpStatus.OK);
    }
}

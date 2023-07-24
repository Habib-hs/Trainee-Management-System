package com.backend.tms.service.Impl;


import com.backend.tms.entity.BatchEntity;
import com.backend.tms.entity.TraineeEntity;
import com.backend.tms.entity.UserEntity;
import com.backend.tms.exception.custom.TraineeNotFoundException;
import com.backend.tms.model.Trainee.TraineeResModel;
import com.backend.tms.model.Trainee.TraineeUpdateReqModel;
import com.backend.tms.repository.BatchRepository;
import com.backend.tms.repository.TraineeRepository;
import com.backend.tms.repository.UserRepository;
import com.backend.tms.service.TraineeService;
import com.backend.tms.utlis.AppConstants;
import com.backend.tms.utlis.FileService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TraineeServiceImp implements TraineeService {

    private final TraineeRepository traineeRepository;
    private final ModelMapper modelMapper;
    private final BatchRepository batchRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<Object> getAllTrainees() {
       List<TraineeEntity> traineeEntityList = traineeRepository.findAll();
       //create a response object
        Map<String, Object> response = new HashMap<>();
        response.put("Total Trainee", traineeEntityList.size());
        response.put("Trainees", traineeEntityList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getAllUnAssignedTrainee() {
        List<Long> assignedTraineeIds = batchRepository.findAll().stream()
                .flatMap(batch -> batch.getTrainees().stream())
                .map(TraineeEntity::getId)
                .collect(Collectors.toList());

        List<TraineeEntity> unassignedTrainees = traineeRepository.findAll().stream()
                .filter(trainee -> !assignedTraineeIds.contains(trainee.getId()))
                .collect(Collectors.toList());

        // Create a response object
        Map<String, Object> response = new HashMap<>();
        response.put("Total Trainee", unassignedTrainees.size());
        response.put("Trainees", unassignedTrainees);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getBatchByTraineeId(Long traineeId) {
        List<BatchEntity> batches = batchRepository.findAll();
        for (BatchEntity batch : batches) {
            for (TraineeEntity trainee : batch.getTrainees()) {
                if (trainee.getId().equals(traineeId)) {
                    return ResponseEntity.ok(batch.getId());
                }
            }
        }
        // If no match is found,
        return ResponseEntity.notFound().build();
    }


    @Override
    public ResponseEntity<Object> getTraineeById(Long traineeId) {
        Optional<TraineeEntity> traineeEntity = traineeRepository.findById(traineeId);
        if(traineeEntity ==null){
            throw new TraineeNotFoundException("trainee not found with ID: " + traineeId);
        }
        TraineeResModel trainee = modelMapper.map(traineeEntity, TraineeResModel.class);
        return new ResponseEntity<>(trainee, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> updateTrainee(Long traineeId, TraineeUpdateReqModel traineeModel) {
        // Check if the trainee exists
        TraineeEntity traineeEntity = traineeRepository.findById(traineeId)
                .orElseThrow(() -> new TraineeNotFoundException("Trainee not found with ID: " + traineeId));

        MultipartFile file = traineeModel.getProfilePicture();
        String fileUrl = null;
        if (file != null && !file.isEmpty()) {
            try {
                fileUrl = FileService.uploadImage(file, AppConstants.IMAGE_UPLOAD_DIR);
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
            // Set the profile picture URL only if the image upload was successful
            traineeEntity.setProfilePicture(fileUrl);
        }

        // Update other attributes of the trainee Entity
        traineeEntity.setFullName(traineeModel.getFullName());
        traineeEntity.setGender(traineeModel.getGender());
        traineeEntity.setDateOfBirth(traineeModel.getDateOfBirth());
        traineeEntity.setContactNumber(traineeModel.getContactNumber());
        traineeEntity.setDegreeName(traineeModel.getDegreeName());
        traineeEntity.setEducationalInstitute(traineeModel.getEducationalInstitute());
        traineeEntity.setCgpa(traineeModel.getCgpa());
        traineeEntity.setPassingYear(traineeModel.getPassingYear());
        traineeEntity.setPresentAddress(traineeModel.getPresentAddress());

        // Save the updated trainee Entity
        traineeRepository.save(traineeEntity);

        // Return a success message
        return new ResponseEntity<>("Trainee updated successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> deleteTrainee(Long traineeId) {
        // Check if the batch exists
       TraineeEntity trainee= traineeRepository.findById(traineeId).orElseThrow(()->new TraineeNotFoundException("Trainee not found"));
        // Retrieve the associated UserEntity
        UserEntity user = trainee.getUser();
        if (user != null) {
            userRepository.delete(user);
        }
        // Delete the trainee
        traineeRepository.deleteById(traineeId);
        return new ResponseEntity<>("Trainee deleted successfully", HttpStatus.OK);
    }

}

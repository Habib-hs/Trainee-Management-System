package com.backend.tms.service.Impl;

import com.backend.tms.entity.CourseEntity;
import com.backend.tms.entity.TraineeEntity;
import com.backend.tms.exception.custom.BatchNotFoundException;
import com.backend.tms.exception.custom.CourseNotFoundException;
import com.backend.tms.exception.custom.TraineeNotFoundException;
import com.backend.tms.model.Course.CourseResModel;
import com.backend.tms.model.Trainee.TraineeReqModel;
import com.backend.tms.model.Trainee.TraineeResModel;
import com.backend.tms.model.Trainee.TraineeUpdateReqModel;
import com.backend.tms.repository.TraineeRepository;
import com.backend.tms.service.TraineeService;
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
public class TraineeServiceImp implements TraineeService {

    private final TraineeRepository traineeRepository;
    private final ModelMapper modelMapper;

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
    public ResponseEntity<Object> getTraineeById(Long traineeId) {
        Optional<TraineeEntity> traineeEntity = traineeRepository.findById(traineeId);
        if(traineeEntity ==null){
            throw new TraineeNotFoundException("Course not found with ID: " + traineeId);
        }
        TraineeResModel trainee = modelMapper.map(traineeEntity, TraineeResModel.class);
        return new ResponseEntity<>(trainee, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> updateTrainee(Long traineeId, TraineeUpdateReqModel traineeModel) {
        // Check if the trainee exists
        TraineeEntity traineeEntity = traineeRepository.findById(traineeId)
                .orElseThrow(() -> new TraineeNotFoundException("Trainee not found with ID: " + traineeId));

        //update the trainee Entity
                traineeEntity.setFullName(traineeModel.getFullName());
                traineeEntity.setProfilePicture(traineeModel.getProfilePicture());
                traineeEntity.setGender(traineeModel.getGender());
                traineeEntity.setDateOfBirth(traineeModel.getDateOfBirth());
                traineeEntity.setContactNumber(traineeModel.getContactNumber());
                traineeEntity.setDegreeName(traineeModel.getDegreeName());
                traineeEntity.setEducationalInstitute(traineeModel.getEducationalInstitute());
                traineeEntity.setCgpa(traineeModel.getCgpa());
                traineeEntity.setCgpa(traineeModel.getPassingYear());
                traineeEntity.setPresentAddress(traineeModel.getPresentAddress());
                traineeRepository.save(traineeEntity);
           // Return a success message
          return new ResponseEntity<>("Trainee updated successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> deleteTrainee(Long traineeId) {
        // Check if the batch exists
        traineeRepository.findById(traineeId).orElseThrow(()->new TraineeNotFoundException("Trainee not found"));
        // Delete the batch
        traineeRepository.deleteById(traineeId);
        return new ResponseEntity<>("Trainee deleted successfully", HttpStatus.OK);
    }
}

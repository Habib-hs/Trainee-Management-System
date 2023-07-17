package com.backend.tms.service.Impl;

import com.backend.tms.entity.BatchEntity;
import com.backend.tms.entity.CourseEntity;
import com.backend.tms.entity.TrainerEntity;
import com.backend.tms.exception.custom.CourseAlreadyExistsException;
import com.backend.tms.exception.custom.CourseNotFoundException;
import com.backend.tms.exception.custom.TrainerNotFoundException;
import com.backend.tms.model.Course.CourseReqModel;
import com.backend.tms.model.Course.CourseResModel;
import com.backend.tms.repository.CourseRepository;
import com.backend.tms.repository.TrainerRepository;
import com.backend.tms.service.CourseService;
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
public class CourseServiceImp implements CourseService {

     private final CourseRepository courseRepository;
     private final ModelMapper modelMapper;
     private final TrainerRepository trainerRepository;

    @Override
    public ResponseEntity<Object> createCourse(CourseReqModel courseModel)
    {
        // if Assigned trainer exist or not
        TrainerEntity assignedTrainer = trainerRepository.findById(courseModel.getAssignedTrainerId())
                .orElseThrow(() -> new TrainerNotFoundException("Trainer not found with ID: " + courseModel.getAssignedTrainerId()));

        // Check if the course with the given name already exists
          if (courseRepository.findByName(courseModel.getName()) !=  null) {
             throw new CourseAlreadyExistsException("Course already exists with the given name");
            }

        // Create CourseEntity
        CourseEntity courseEntity =  modelMapper.map(courseModel, CourseEntity.class);
        CourseEntity savedCourse = courseRepository.save(courseEntity);
        assignedTrainer.getCourses().add(savedCourse);
        trainerRepository.save(assignedTrainer);

        // If the save operation is successful, return a success message
        return new ResponseEntity<>("Course added successfully", HttpStatus.CREATED);

    }

    @Override
    public ResponseEntity<Object> deleteCourse(Long courseId) {
        return null;
    }

    @Override
    public ResponseEntity<Object> updateCourse(Long courseId, CourseReqModel courseModel) {
        // Check if the course exists
        CourseEntity courseEntity = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with ID: " + courseId));

        // Update the batch details
        courseEntity.setName(courseModel.getName());
       courseEntity.setDescription(courseModel.getDescription());
       courseEntity.setAssignedTrainerId(courseModel.getAssignedTrainerId());
       courseEntity.setTrainerName(courseModel.getTrainerName());
       courseRepository.save(courseEntity);

        // Return a success message
        return new ResponseEntity<>("Course updated successfully", HttpStatus.OK);
    }


    @Override
    public ResponseEntity<Object> getCourseById(Long courseId) {
        // Get the course by ID
        Optional<CourseEntity> courseOptional = courseRepository.findById(courseId);
        CourseEntity courseEntity = courseOptional.orElseThrow(() -> new CourseNotFoundException("Course not found with ID: " + courseId));

        CourseResModel courseResModel = modelMapper.map(courseEntity, CourseResModel.class);

        // Return the course response model
        return new ResponseEntity<>(courseResModel, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getAllCourses() {
        List<CourseEntity> courseEntities = courseRepository.findAll();
        // Create a response object
        Map<String, Object> response = new HashMap<>();
        response.put("Total Course", courseEntities.size());
        response.put("Courses", courseEntities);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

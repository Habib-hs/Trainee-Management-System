package com.backend.tms.service.Impl;

import com.backend.tms.entity.BatchEntity;
import com.backend.tms.entity.CourseEntity;
import com.backend.tms.entity.TrainerEntity;
import com.backend.tms.exception.custom.CourseAlreadyExistsException;
import com.backend.tms.exception.custom.TrainerNotFoundException;
import com.backend.tms.model.Course.CourseReqModel;
import com.backend.tms.repository.CourseRepository;
import com.backend.tms.repository.TrainerRepository;
import com.backend.tms.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

       //saved the course to the trainerEntity class
        assignedTrainer.getCourses().add(savedCourse);
        trainerRepository.save(assignedTrainer);

        // If the save operation is successful, return a success message
        return new ResponseEntity<>("Course added successfully", HttpStatus.CREATED);

    }

    @Override
    public ResponseEntity<Object> deleteCourse(Long courseId) {
        return null;
    }
}

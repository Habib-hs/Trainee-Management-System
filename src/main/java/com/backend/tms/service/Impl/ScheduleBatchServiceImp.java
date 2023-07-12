package com.backend.tms.service.Impl;


import com.backend.tms.entity.BatchEntity;
import com.backend.tms.entity.CourseEntity;
import com.backend.tms.entity.ScheduleBatchEntity;
import com.backend.tms.exception.custom.BatchNotFoundException;
import com.backend.tms.exception.custom.CourseNotFoundException;
import com.backend.tms.model.ScheduleBatch.ScheduleBatchReqModel;
import com.backend.tms.repository.BatchRepository;
import com.backend.tms.repository.CourseRepository;
import com.backend.tms.repository.ScheduleRepository;
import com.backend.tms.service.ScheduleBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ScheduleBatchServiceImp implements ScheduleBatchService {

    private final ScheduleRepository scheduleRepository;
    private final CourseRepository courseRepository;
    private final BatchRepository batchRepository;

    @Override
    public ResponseEntity<Object> createScheduleBatch(ScheduleBatchReqModel scheduleBatchModel) {
        ScheduleBatchEntity scheduleBatchEntity = mapToScheduleBatchEntity(scheduleBatchModel);
        ScheduleBatchEntity savedScheduleBatch = scheduleRepository.save(scheduleBatchEntity);
        if (savedScheduleBatch != null) {
            return new ResponseEntity<>("A program scheduled successfully", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Failed to schedule the program", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    private ScheduleBatchEntity mapToScheduleBatchEntity(ScheduleBatchReqModel scheduleBatchModel) {
        ScheduleBatchEntity scheduleBatchEntity = new ScheduleBatchEntity();
        scheduleBatchEntity.setCourseName(scheduleBatchModel.getCourseName());
        scheduleBatchEntity.setCourseType(scheduleBatchModel.getCourseType());
        scheduleBatchEntity.setStartDate(scheduleBatchModel.getStartDate());
        scheduleBatchEntity.setEndDate(scheduleBatchModel.getEndDate());

        // Map CourseEntity
        String courseIdString = scheduleBatchModel.getCourseId();
        Long courseId = Long.parseLong(courseIdString);
        Optional<CourseEntity> courseEntityOptional = courseRepository.findById(courseId);

        if (courseEntityOptional.isPresent()) {
            CourseEntity courseEntity = courseEntityOptional.get();
            scheduleBatchEntity.setCourse(courseEntity);
        }

        // Map BatchEntities
        List<Long> batchIds = scheduleBatchModel.getBatchesIds();
        Set <BatchEntity> batchEntities= new HashSet<>();
        for (Long batchId : batchIds){
            var data = batchRepository.findById(batchId);
            if (data.isPresent()){
                batchEntities.add(data.get());
            }
        }
        scheduleBatchEntity.setBatches(batchEntities);
        return scheduleBatchEntity;
    }

}

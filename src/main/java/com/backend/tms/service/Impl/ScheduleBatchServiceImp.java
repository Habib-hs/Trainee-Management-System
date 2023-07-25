package com.backend.tms.service.Impl;

import com.backend.tms.entity.BatchEntity;
import com.backend.tms.entity.CourseEntity;
import com.backend.tms.entity.ScheduleBatchEntity;
import com.backend.tms.entity.TrainerEntity;
import com.backend.tms.exception.custom.BatchNotFoundException;
import com.backend.tms.exception.custom.CourseAlreadyExistsException;
import com.backend.tms.model.ScheduleBatch.ScheduleBatchReqModel;
import com.backend.tms.repository.BatchRepository;
import com.backend.tms.repository.CourseRepository;
import com.backend.tms.repository.ScheduleRepository;
import com.backend.tms.repository.TrainerRepository;
import com.backend.tms.service.ScheduleBatchService;
import com.backend.tms.utlis.ValidationUtlis;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ScheduleBatchServiceImp implements ScheduleBatchService {

    private final ScheduleRepository scheduleRepository;
    private final CourseRepository courseRepository;
    private final BatchRepository batchRepository;
    private final TrainerRepository trainerRepository;

    @Override
    public ResponseEntity<Object> createScheduleBatch(ScheduleBatchReqModel scheduleBatchModel) {

        // Check if the courseId is valid
        ScheduleBatchEntity existingSchedule = scheduleRepository.findByCourseName(scheduleBatchModel.getCourseName());
        if (existingSchedule != null) {
            throw new CourseAlreadyExistsException("The course is already Scheduled!");
        }

         if(!ValidationUtlis.isBatchDurationValid(scheduleBatchModel.getStartDate(), scheduleBatchModel.getEndDate())){
             return new ResponseEntity<>("The Time range should not longer than 4 month", HttpStatus.BAD_REQUEST);
         }

         if(!ValidationUtlis.isDateRangeValid(scheduleBatchModel.getStartDate(), scheduleBatchModel.getEndDate())){
             return new ResponseEntity<>("Ending Date can't same or less than Starting Date", HttpStatus.BAD_REQUEST);
         }

       //separate validation for Common & Domain Specific Course
        if (scheduleBatchModel.getCourseType().equals("Common")) {
            if (ValidationUtlis.hasCommonCourseConflicts(scheduleBatchModel, scheduleRepository)) {
                return new ResponseEntity<>("The course overlaps with an existing common course", HttpStatus.BAD_REQUEST);
            }
        } else {
            if (ValidationUtlis.hasDomainCourseConflicts(scheduleBatchModel, batchRepository)) {
                return new ResponseEntity<>("The course overlaps with an existing course for the same batch", HttpStatus.BAD_REQUEST);
            }
        }

        ScheduleBatchEntity scheduleBatchEntity = mapToScheduleBatchEntity(scheduleBatchModel);
        scheduleRepository.save(scheduleBatchEntity);
        return new ResponseEntity<>("A program scheduled successfully", HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Object> getScheduleNames() {
        List<ScheduleBatchEntity> scheduleEntities = scheduleRepository.findAll();

        // Create a response object
        List<Map<String, Object>> schedules = new ArrayList<>();
        for (ScheduleBatchEntity schedule : scheduleEntities) {
            Map<String, Object> scheduleData = new HashMap<>();
            scheduleData.put("id", schedule.getId());
            scheduleData.put("name", schedule.getCourseName());
            scheduleData.put("startingDate", schedule.getStartDate());
            scheduleData.put("endingDate", schedule.getEndDate());
            scheduleData.put("courseType", schedule.getCourseType());

            //getting the trainer name
            TrainerEntity trainer = trainerRepository.findByCoursesName(schedule.getCourseName());
            if (trainer != null) {
                scheduleData.put("trainerId", trainer.getId());
            }
            // Getting batches info.
            Set<BatchEntity> batches = schedule.getBatches();
            List<String> batchNames = new ArrayList<>();
            for (BatchEntity batch : batches) {
                batchNames.add(batch.getBatchName());
            }
            scheduleData.put("batchNames", batchNames);

            schedules.add(scheduleData);
        }
        // Create the final response
        Map<String, Object> response = new HashMap<>();
        response.put("Total Schedule", schedules.size());
        response.put("Schedules", schedules);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private ScheduleBatchEntity mapToScheduleBatchEntity(ScheduleBatchReqModel scheduleBatchModel) {
        ScheduleBatchEntity scheduleBatchEntity = new ScheduleBatchEntity();
        scheduleBatchEntity.setCourseName(scheduleBatchModel.getCourseName());
        scheduleBatchEntity.setCourseType(scheduleBatchModel.getCourseType());
        scheduleBatchEntity.setStartDate(scheduleBatchModel.getStartDate());
        scheduleBatchEntity.setEndDate(scheduleBatchModel.getEndDate());

        String courseType = scheduleBatchModel.getCourseType();
        if ("Common".equals(courseType)) {
            mapCommonOrDomainCourse(scheduleBatchEntity, true, null);
        } else {
            mapCommonOrDomainCourse(scheduleBatchEntity, false, scheduleBatchModel);
        }

        return scheduleBatchEntity;
    }

    private void mapCommonOrDomainCourse(ScheduleBatchEntity scheduleBatchEntity, boolean isCommon, ScheduleBatchReqModel scheduleBatchModel) {
        Set<BatchEntity> batchEntities = new HashSet<>();

        if (isCommon) {
            List<BatchEntity> batchEntityList = batchRepository.findAll();
            batchEntities.addAll(batchEntityList);
        } else {
            List<Long> batchIds = scheduleBatchModel.getBatchesIds();
            for (Long batchId : batchIds) {
                BatchEntity batch = batchRepository.findById(batchId)
                        .orElseThrow(() -> new BatchNotFoundException("Batch not found with ID: " + batchId));
                batchEntities.add(batch);
            }
        }

        scheduleBatchEntity.setBatches(batchEntities);
    }



}

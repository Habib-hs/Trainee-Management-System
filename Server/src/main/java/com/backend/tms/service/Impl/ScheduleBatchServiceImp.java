package com.backend.tms.service.Impl;

import com.backend.tms.entity.BatchEntity;
import com.backend.tms.entity.CourseEntity;
import com.backend.tms.entity.ScheduleBatchEntity;
import com.backend.tms.entity.TrainerEntity;
import com.backend.tms.exception.custom.BatchNotFoundException;
import com.backend.tms.exception.custom.CourseAlreadyExistsException;
import com.backend.tms.exception.custom.CourseNotFoundException;
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
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

            // Format startingDate and endingDate to a more readable string format
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
            Date startingDate = schedule.getStartDate();
            LocalDateTime startingDateTime = startingDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            scheduleData.put("startingDate", startingDateTime.format(dateFormatter));
            Date endingDate = schedule.getEndDate();
            LocalDateTime endingDateTime = endingDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            scheduleData.put("endingDate", endingDateTime.format(dateFormatter));

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

    @Override
    public ResponseEntity<Object> updateScheduleBatch(Long scheduleId, ScheduleBatchReqModel scheduleBatchModel) {
        ScheduleBatchEntity existingSchedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new BatchNotFoundException("Schedule not found with ID: " + scheduleId));

        // Check if the courseId is valid
        ScheduleBatchEntity existingScheduleByCourseName = scheduleRepository.findByCourseName(scheduleBatchModel.getCourseName());
        if (existingScheduleByCourseName != null && !existingScheduleByCourseName.getId().equals(scheduleId)) {
            throw new CourseAlreadyExistsException("The course is already Scheduled!");
        }

        if (!ValidationUtlis.isBatchDurationValid(scheduleBatchModel.getStartDate(), scheduleBatchModel.getEndDate())) {
            return new ResponseEntity<>("The Time range should not longer than 4 months", HttpStatus.BAD_REQUEST);
        }

        if (!ValidationUtlis.isDateRangeValid(scheduleBatchModel.getStartDate(), scheduleBatchModel.getEndDate())) {
            return new ResponseEntity<>("Ending Date can't be the same or less than Starting Date", HttpStatus.BAD_REQUEST);
        }

        // Separate validation for Common & Domain Specific Course
        if (scheduleBatchModel.getCourseType().equals("Common")) {
            if (ValidationUtlis.hasCommonCourseConflicts(scheduleBatchModel, scheduleRepository)) {
                return new ResponseEntity<>("The course overlaps with an existing common course", HttpStatus.BAD_REQUEST);
            }
        } else {
            if (ValidationUtlis.hasDomainCourseConflicts(scheduleBatchModel, batchRepository)) {
                return new ResponseEntity<>("The course overlaps with an existing course for the same batch", HttpStatus.BAD_REQUEST);
            }
        }

        // Update the scheduleBatchEntity with the new data
        existingSchedule.setCourseName(scheduleBatchModel.getCourseName());
        existingSchedule.setCourseType(scheduleBatchModel.getCourseType());
        existingSchedule.setStartDate(scheduleBatchModel.getStartDate());
        existingSchedule.setEndDate(scheduleBatchModel.getEndDate());

        String courseType = scheduleBatchModel.getCourseType();
        if ("Common".equals(courseType)) {
            mapCommonOrDomainCourse(existingSchedule, true, null);
        } else {
            mapCommonOrDomainCourse(existingSchedule, false, scheduleBatchModel);
        }

        scheduleRepository.save(existingSchedule);
        return new ResponseEntity<>("Schedule updated successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> deleteScheduleBatch(Long scheduleId) {
        ScheduleBatchEntity existingSchedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new BatchNotFoundException("Schedule not found with ID: " + scheduleId));

        // Remove the relationship between ScheduleBatchEntity and BatchEntity
        existingSchedule.getBatches().clear();
        scheduleRepository.save(existingSchedule);
        scheduleRepository.delete(existingSchedule);

        return new ResponseEntity<>("Schedule deleted successfully", HttpStatus.OK);
    }


    private ScheduleBatchEntity mapToScheduleBatchEntity(ScheduleBatchReqModel scheduleBatchModel) {
        ScheduleBatchEntity scheduleBatchEntity = new ScheduleBatchEntity();
        scheduleBatchEntity.setCourseName(scheduleBatchModel.getCourseName());
        scheduleBatchEntity.setCourseType(scheduleBatchModel.getCourseType());
        scheduleBatchEntity.setStartDate(scheduleBatchModel.getStartDate());
        scheduleBatchEntity.setEndDate(scheduleBatchModel.getEndDate());

        //saved the course to the schedule
        CourseEntity course=courseRepository.findById(scheduleBatchModel.getCourseId())
                .orElseThrow(()->new CourseNotFoundException("Course not Found!"));

        scheduleBatchEntity.setCourse(course);

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

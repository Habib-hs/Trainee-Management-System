package com.backend.tms.service.Impl;

import com.backend.tms.entity.BatchEntity;
import com.backend.tms.entity.CourseEntity;
import com.backend.tms.entity.ScheduleBatchEntity;
import com.backend.tms.exception.custom.BatchNotFoundException;
import com.backend.tms.exception.custom.CourseAlreadyExistsException;
import com.backend.tms.model.ScheduleBatch.ScheduleBatchReqModel;
import com.backend.tms.repository.BatchRepository;
import com.backend.tms.repository.CourseRepository;
import com.backend.tms.repository.ScheduleRepository;
import com.backend.tms.service.ScheduleBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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


        String courseType = scheduleBatchModel.getCourseType();
        if (courseType.equals("common")) {
            // Handle scheduling for common type course
            return handleCommonCourseScheduling(scheduleBatchModel);
        }

        //time validation
        LocalDateTime startDate = scheduleBatchModel.getStartDate().toLocalDateTime();
        LocalDateTime endDate = scheduleBatchModel.getEndDate().toLocalDateTime();

        // Check that the starting date is not greater than the ending date
        if (startDate.isAfter(endDate)) {
            return new ResponseEntity<>("Starting date cannot be later than the ending date", HttpStatus.BAD_REQUEST);
        }

        // Check that the time schedule length does not exceed four months
        if (startDate.plusMonths(4).isBefore(endDate)) {
            return new ResponseEntity<>("Time schedule length should not exceed four months", HttpStatus.BAD_REQUEST);
        }

        // Check for available time slot within the batch
        List<Long> batchIds = scheduleBatchModel.getBatchesIds();
        Set<ScheduleBatchEntity> existingSchedules = new HashSet<>();

        for (Long batchId : batchIds) {
            BatchEntity batch = batchRepository.findById(batchId)
                    .orElseThrow(() -> new BatchNotFoundException("Batch not found with ID: " + batchId));
            existingSchedules.addAll(batch.getSchedulePrograms());
        }

        for (Long batchId : batchIds) {
            BatchEntity batch = batchRepository.findById(batchId)
                    .orElseThrow(() -> new BatchNotFoundException("Batch not found with ID: " + batchId));

            LocalDateTime batchStartDate = batch.getStartDate().toLocalDateTime();
            LocalDateTime batchEndDate = batch.getEndDate().toLocalDateTime();

            // Check if the requested course's start date is after the batch start date and before the batch end date
            if (startDate.isAfter(batchStartDate) && startDate.isBefore(batchEndDate)) {
                // Check if the requested course's end date is after the batch end date
                if (endDate.isAfter(batchEndDate)) {
                    return new ResponseEntity<>("The course end date exceeds the batch end date for Batch: " + batch.getBatchName(), HttpStatus.BAD_REQUEST);
                } else{
                    for (ScheduleBatchEntity existingSchedule : existingSchedules) {
                        LocalDateTime existingStartDate = existingSchedule.getStartDate();
                        LocalDateTime existingEndDate = existingSchedule.getEndDate();

                        // Check if the requested course's start date falls within an existing schedule
                        if (startDate.isAfter(existingStartDate) && startDate.isBefore(existingEndDate)) {
                            return new ResponseEntity<>("The course overlaps with an existing course for Batch: " + batch.getBatchName(), HttpStatus.BAD_REQUEST);
                        }

                        // Check if the requested course's end date falls within an existing schedule
                        if (endDate.isAfter(existingStartDate) && endDate.isBefore(existingEndDate)) {
                            return new ResponseEntity<>("The course overlaps with an existing course for Batch: " + batch.getBatchName(), HttpStatus.BAD_REQUEST);
                        }

                        // Check if the requested course's start date and end date enclose an existing schedule
                        if (startDate.isBefore(existingStartDate) && endDate.isAfter(existingEndDate)) {
                            return new ResponseEntity<>("The course overlaps with an existing course for Batch: " + batch.getBatchName(), HttpStatus.BAD_REQUEST);
                        }
                    }

                    // Schedule the course within the available time slot
                    ScheduleBatchEntity scheduleBatchEntity = mapToScheduleBatchEntity(scheduleBatchModel);
                    ScheduleBatchEntity savedScheduleBatch = scheduleRepository.save(scheduleBatchEntity);
                    if (savedScheduleBatch != null) {
                        return new ResponseEntity<>("A program scheduled successfully", HttpStatus.CREATED);
                    } else {
                        return new ResponseEntity<>("Failed to schedule the program", HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
            } else {
                return new ResponseEntity<>("The course start date does not fall within the available time slot for Batch: " + batch.getBatchName(), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("Failed to schedule the program", HttpStatus.INTERNAL_SERVER_ERROR);
    }



    private ResponseEntity<Object> handleCommonCourseScheduling(ScheduleBatchReqModel scheduleBatchModel) {
        LocalDateTime startDate = scheduleBatchModel.getStartDate().toLocalDateTime();
        LocalDateTime endDate = scheduleBatchModel.getEndDate().toLocalDateTime();

        // Check that the starting date is not greater than the ending date
        if (startDate.isAfter(endDate)) {
            return new ResponseEntity<>("Starting date cannot be later than the ending date", HttpStatus.BAD_REQUEST);
        }

        // Check that the time schedule length does not exceed four months
        if (startDate.plusMonths(4).isBefore(endDate)) {
            return new ResponseEntity<>("Time schedule length should not exceed four months", HttpStatus.BAD_REQUEST);
        }

        // Check for conflicts with existing common courses
        Set<ScheduleBatchEntity> existingSchedules = new HashSet<>();
        existingSchedules.addAll(scheduleRepository.findByCourseType("common"));

        for (ScheduleBatchEntity existingSchedule : existingSchedules) {
            LocalDateTime existingStartDate = existingSchedule.getStartDate();
            LocalDateTime existingEndDate = existingSchedule.getEndDate();

            // Check if the requested course's start date falls within an existing schedule
            if (startDate.isAfter(existingStartDate) && startDate.isBefore(existingEndDate)) {
                return new ResponseEntity<>("The course overlaps with an existing common course", HttpStatus.BAD_REQUEST);
            }

            // Check if the requested course's end date falls within an existing schedule
            if (endDate.isAfter(existingStartDate) && endDate.isBefore(existingEndDate)) {
                return new ResponseEntity<>("The course overlaps with an existing common course", HttpStatus.BAD_REQUEST);
            }

            // Check if the requested course's start date and end date enclose an existing schedule
            if (startDate.isBefore(existingStartDate) && endDate.isAfter(existingEndDate)) {
                return new ResponseEntity<>("The course overlaps with an existing common course", HttpStatus.BAD_REQUEST);
            }
        }

        // Schedule the common course across all batches
        List<Long> batchIds = scheduleBatchModel.getBatchesIds();
        Set<BatchEntity> batches = new HashSet<>();

        for (Long batchId : batchIds) {
            BatchEntity batch = batchRepository.findById(batchId)
                    .orElseThrow(() -> new BatchNotFoundException("Batch not found with ID: " + batchId));

            batches.add(batch);
        }
        // Create a new schedule for the common course with all batches
        ScheduleBatchEntity scheduleBatchEntity = mapToScheduleBatchEntity(scheduleBatchModel);
        scheduleBatchEntity.setBatches(batches);
        scheduleRepository.save(scheduleBatchEntity);

        return new ResponseEntity<>("A common course scheduled successfully", HttpStatus.CREATED);
    }

    private ScheduleBatchEntity mapToScheduleBatchEntity(ScheduleBatchReqModel scheduleBatchModel) {
        ScheduleBatchEntity scheduleBatchEntity = new ScheduleBatchEntity();
        scheduleBatchEntity.setCourseName(scheduleBatchModel.getCourseName());
        scheduleBatchEntity.setCourseType(scheduleBatchModel.getCourseType());
        scheduleBatchEntity.setStartDate(scheduleBatchModel.getStartDate().toLocalDateTime());
        scheduleBatchEntity.setEndDate(scheduleBatchModel.getEndDate().toLocalDateTime());
        // Map CourseEntity
        String courseIdString = scheduleBatchModel.getCourseId();
        Long courseId = Long.parseLong(courseIdString);
        Optional<CourseEntity> courseEntityOptional = courseRepository.findById(courseId);

        if (courseEntityOptional.isPresent()) {
            CourseEntity courseEntity = courseEntityOptional.get();
            scheduleBatchEntity.setCourse(courseEntity);
        }

        List<Long> batchIds = scheduleBatchModel.getBatchesIds();
        Set<BatchEntity> batchEntities = new HashSet<>();

        for (Long batchId : batchIds) {
            var data = batchRepository.findById(batchId);
            data.ifPresent(batchEntities::add);
        }

        scheduleBatchEntity.setBatches(batchEntities);
        scheduleBatchEntity.setBatches(batchEntities);
        return scheduleBatchEntity;
    }

}

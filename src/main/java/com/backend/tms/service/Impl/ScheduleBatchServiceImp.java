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

        if (scheduleBatchModel.getCourseType().equals("Common")) {
            if (!isCommonCourseTimeValid(scheduleBatchModel)) {
                return new ResponseEntity<>("Invalid time schedule for common course", HttpStatus.BAD_REQUEST);
            }
            if (hasCommonCourseConflicts(scheduleBatchModel)) {
                return new ResponseEntity<>("The course overlaps with an existing common course", HttpStatus.BAD_REQUEST);
            }
        } else {
            if (!isDomainCourseTimeValid(scheduleBatchModel)) {
                return new ResponseEntity<>("Invalid time schedule for domain course", HttpStatus.BAD_REQUEST);
            }
            if (hasDomainCourseConflicts(scheduleBatchModel)) {
                return new ResponseEntity<>("The course overlaps with an existing course for some batch", HttpStatus.BAD_REQUEST);
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


    private boolean isCommonCourseTimeValid(ScheduleBatchReqModel scheduleBatchModel) {
        LocalDateTime startDate = scheduleBatchModel.getStartDate().toLocalDateTime();
        LocalDateTime endDate = scheduleBatchModel.getEndDate().toLocalDateTime();

        return startDate.isBefore(endDate) && !startDate.plusMonths(4).isBefore(endDate);
    }

    private boolean isDomainCourseTimeValid(ScheduleBatchReqModel scheduleBatchModel) {
        LocalDateTime startDate = scheduleBatchModel.getStartDate().toLocalDateTime();
        LocalDateTime endDate = scheduleBatchModel.getEndDate().toLocalDateTime();

        return startDate.isBefore(endDate);
    }

    private boolean hasCommonCourseConflicts(ScheduleBatchReqModel scheduleBatchModel) {
        LocalDateTime startDate = scheduleBatchModel.getStartDate().toLocalDateTime();
        LocalDateTime endDate = scheduleBatchModel.getEndDate().toLocalDateTime();

        List<ScheduleBatchEntity> existingSchedules = scheduleRepository.findByCourseType("Common");
        for (ScheduleBatchEntity existingSchedule : existingSchedules) {
            if (isOverlapping(startDate, endDate, existingSchedule.getStartDate(), existingSchedule.getEndDate())) {
                return true;
            }
        }
        return false;
    }

    private boolean hasDomainCourseConflicts(ScheduleBatchReqModel scheduleBatchModel) {
        LocalDateTime startDate = scheduleBatchModel.getStartDate().toLocalDateTime();
        LocalDateTime endDate = scheduleBatchModel.getEndDate().toLocalDateTime();

        List<Long> batchIds = scheduleBatchModel.getBatchesIds();
        for (Long batchId : batchIds) {
            BatchEntity batch = batchRepository.findById(batchId)
                    .orElseThrow(() -> new BatchNotFoundException("Batch not found with ID: " + batchId));

            for (ScheduleBatchEntity existingSchedule : batch.getSchedulePrograms()) {
                if (existingSchedule.getCourseType().equals("Common")) {
                    continue; // Skip common course schedules
                }

                if (isOverlapping(startDate, endDate, existingSchedule.getStartDate(), existingSchedule.getEndDate())) {
                    return true; // Conflict with domain-specific course
                }
            }
        }

        return false;
    }

    private boolean isOverlapping(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    private ScheduleBatchEntity mapToScheduleBatchEntity(ScheduleBatchReqModel scheduleBatchModel) {
        ScheduleBatchEntity scheduleBatchEntity = new ScheduleBatchEntity();
        scheduleBatchEntity.setCourseName(scheduleBatchModel.getCourseName());
        scheduleBatchEntity.setCourseType(scheduleBatchModel.getCourseType());
        scheduleBatchEntity.setStartDate(scheduleBatchModel.getStartDate().toLocalDateTime());
        scheduleBatchEntity.setEndDate(scheduleBatchModel.getEndDate().toLocalDateTime());

        String courseIdString = scheduleBatchModel.getCourseId();
        Long courseId = Long.parseLong(courseIdString);
        Optional<CourseEntity> courseEntityOptional = courseRepository.findById(courseId);

        if (courseEntityOptional.isPresent()) {
            CourseEntity courseEntity = courseEntityOptional.get();
            scheduleBatchEntity.setCourse(courseEntity);
        }


        List<Long> batchIds = scheduleBatchModel.getBatchesIds();
        Set<BatchEntity> batchEntities = new HashSet<>();

        if (scheduleBatchModel.getCourseType().equals("Common")){
            List<BatchEntity> batchEntityList = batchRepository.findAll();
            // System.out.println(batchEntityList.size());
            batchEntities = new HashSet<>(batchEntityList);
            scheduleBatchEntity.setBatches(batchEntities);
        }else{
            for (Long batchId : batchIds) {
                BatchEntity batch = batchRepository.findById(batchId)
                        .orElseThrow(() -> new BatchNotFoundException("Batch not found with ID: " + batchId));
                batchEntities.add(batch);
                scheduleBatchEntity.setBatches(batchEntities);
            }
        }
        return scheduleBatchEntity;
    }
}

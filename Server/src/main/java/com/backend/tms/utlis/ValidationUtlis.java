package com.backend.tms.utlis;
import com.backend.tms.entity.BatchEntity;
import com.backend.tms.entity.ScheduleBatchEntity;
import com.backend.tms.exception.custom.BatchNotFoundException;
import com.backend.tms.model.ScheduleBatch.ScheduleBatchReqModel;
import com.backend.tms.repository.BatchRepository;
import com.backend.tms.repository.ScheduleRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class ValidationUtlis {

    public static boolean isBatchDurationValid(Date startDate, Date endDate) {

        // Calculate the difference in days between start date and end date
        long durationInDays = TimeUnit.MILLISECONDS.toDays(endDate.getTime() - startDate.getTime());

        // Check if the duration is between 122 days (4 months)
        return durationInDays <= 122;
    }

    public static boolean isDateRangeValid(Date startDate, Date endDate) {
        LocalDate startLocalDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endLocalDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return !endLocalDate.isBefore(startLocalDate);
    }

    public static boolean hasCommonCourseConflicts(ScheduleBatchReqModel scheduleBatchModel, ScheduleRepository scheduleRepository) {
        Date startDate = scheduleBatchModel.getStartDate();
        Date endDate = scheduleBatchModel.getEndDate();

        List<ScheduleBatchEntity> existingSchedules = scheduleRepository.findByCourseType("Common");
        for (ScheduleBatchEntity existingSchedule : existingSchedules) {
            if (isOverlapping(startDate, endDate, existingSchedule.getStartDate(), existingSchedule.getEndDate())) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasDomainCourseConflicts(ScheduleBatchReqModel scheduleBatchModel, BatchRepository batchRepository) {
        Date startDate = scheduleBatchModel.getStartDate();
        Date endDate = scheduleBatchModel.getEndDate();

        List<Long> batchIds = scheduleBatchModel.getBatchesIds();
        for (Long batchId : batchIds) {
            BatchEntity batch = batchRepository.findById(batchId)
                    .orElseThrow(() -> new BatchNotFoundException("Batch not found with ID: " + batchId));

            for (ScheduleBatchEntity existingSchedule : batch.getSchedulePrograms()) {
                if (existingSchedule.getCourseType().equals("Common")) {
                    continue;
                }

                if (isOverlapping(startDate, endDate, existingSchedule.getStartDate(), existingSchedule.getEndDate())) {
                    return true; // Conflict with domain-specific course
                }
            }
        }

        return false;
    }

    private static boolean isOverlapping(Date start1, Date end1, Date start2, Date end2) {
        return start1.before(end2) && start2.before(end1);
    }

}

package com.backend.tms.repository;

import com.backend.tms.entity.AssignmentEntity;
import com.backend.tms.entity.ClassroomEntity;
import com.backend.tms.entity.ScheduleBatchEntity;
import com.backend.tms.entity.TraineeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleBatchEntity, Long> {
    // Add any additional methods specific to the TraineeEntity if needed
    Optional<ScheduleBatchEntity> findById(Long scheduleId);
    List<ScheduleBatchEntity> findByCourseType(String courseType);
    ScheduleBatchEntity findByAssignmentsContaining(AssignmentEntity assignmentEntity);
}
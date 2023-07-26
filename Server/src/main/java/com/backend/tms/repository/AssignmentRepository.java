package com.backend.tms.repository;

import com.backend.tms.entity.AssignmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<AssignmentEntity, Long> {
    // You can add custom query methods if needed
    List<AssignmentEntity> findByTrainerId(Long trainerId);
}
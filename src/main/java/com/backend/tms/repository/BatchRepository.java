package com.backend.tms.repository;

import com.backend.tms.entity.BatchEntity;
import com.backend.tms.entity.TraineeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public  interface BatchRepository extends  JpaRepository<BatchEntity, Long> {
    BatchEntity findByBatchName(String batchName);
    Set<BatchEntity> findByTrainees(TraineeEntity trainee);
}

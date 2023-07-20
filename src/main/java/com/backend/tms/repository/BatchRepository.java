package com.backend.tms.repository;

import com.backend.tms.entity.BatchEntity;
import com.backend.tms.entity.TraineeEntity;
import com.backend.tms.entity.TrainerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public  interface BatchRepository extends  JpaRepository<BatchEntity, Long> {
    BatchEntity findByBatchName(String batchName);
}

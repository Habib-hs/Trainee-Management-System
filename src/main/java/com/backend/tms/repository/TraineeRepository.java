package com.backend.tms.repository;

import com.backend.tms.entity.TraineeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TraineeRepository extends JpaRepository<TraineeEntity, Long> {
    // Add any additional methods specific to the TraineeEntity if needed

    boolean existsById(Long id);
    TraineeEntity findByEmail(String email);
}

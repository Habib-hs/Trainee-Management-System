package com.backend.tms.repository;


import com.backend.tms.entity.SubmitAssignmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmitAssignmentRepository extends JpaRepository<SubmitAssignmentEntity, Long> {

}

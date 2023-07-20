package com.backend.tms.repository;

import com.backend.tms.entity.ClassroomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassroomRepository extends JpaRepository<ClassroomEntity, Long> {
    void deleteById(Long id);
    ClassroomEntity findByClassName(String classroomName);

}
package com.backend.tms.repository;

import com.backend.tms.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long> {
    // You can add custom query methods if needed
    CourseEntity findByName (String name);



}

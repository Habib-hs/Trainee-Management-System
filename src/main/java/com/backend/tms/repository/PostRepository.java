package com.backend.tms.repository;
import com.backend.tms.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
    List<PostEntity> findByClassroomId(Long classroomId);
}
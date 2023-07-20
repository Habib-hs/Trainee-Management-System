package com.backend.tms.repository;

import com.backend.tms.entity.NoticeEntity;
import com.backend.tms.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {
    List<NoticeEntity> findByClassroomId(Long classroomId);
}

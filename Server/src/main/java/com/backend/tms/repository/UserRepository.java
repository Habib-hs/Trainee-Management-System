package com.backend.tms.repository;

import com.backend.tms.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findById(long id);
    UserEntity findByEmail(String email);
    boolean existsByEmail(String email);
}

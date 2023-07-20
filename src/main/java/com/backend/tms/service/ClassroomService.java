package com.backend.tms.service;

import org.springframework.http.ResponseEntity;

public interface ClassroomService {
    ResponseEntity<Object> getAllClassroomName();
    ResponseEntity<Object> getClassroomByName(String classroomName);
}

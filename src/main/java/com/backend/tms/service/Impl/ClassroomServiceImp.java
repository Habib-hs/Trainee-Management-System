package com.backend.tms.service.Impl;


import com.backend.tms.entity.BatchEntity;
import com.backend.tms.entity.ClassroomEntity;
import com.backend.tms.repository.ClassroomRepository;
import com.backend.tms.service.ClassroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ClassroomServiceImp implements ClassroomService {
     private final ClassroomRepository classroomRepository;
    @Override
    public ResponseEntity<Object> getAllClassroomName() {
        List<ClassroomEntity> classroomEntities = classroomRepository.findAll();
        // Create a response object
        List<Map<String, Object>> classrooms = new ArrayList<>();
        for (ClassroomEntity classroom : classroomEntities) {
            Map<String, Object>  classroomData = new HashMap<>();
            classroomData.put("id", classroom.getId());
            classroomData.put("name", classroom.getClassName());
            classrooms.add(classroomData);
        }
        // Create the final response
        Map<String, Object> response = new HashMap<>();
        response.put("Total Classrooms", classrooms.size());
        response.put("Classroom", classrooms);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getClassroomByName(String classroomName) {
        ClassroomEntity classroom = classroomRepository.findByClassName(classroomName);
        if (classroom != null) {
            return new ResponseEntity<>(classroom.getId(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Classroom not found", HttpStatus.NOT_FOUND);
        }
    }
}

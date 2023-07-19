package com.backend.tms.controller;

import com.backend.tms.service.ClassroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/classroom")
@RequiredArgsConstructor
public class ClassroomController {
    private final ClassroomService classroomService;
    @GetMapping("/get")
    public ResponseEntity<Object> getAllClassroom() {
        return classroomService.getAllClassroomName();
    }
}

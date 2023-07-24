package com.backend.tms.controller;

import com.backend.tms.model.Course.CourseReqModel;
import com.backend.tms.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> createCourse(@RequestBody CourseReqModel courseModel) {
        return courseService.createCourse(courseModel);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteCourse(@PathVariable("id") Long courseId) {
        return courseService.deleteCourse(courseId);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> updateCourse(@PathVariable("id") Long courseId, @RequestBody CourseReqModel courseModel) {
        return courseService.updateCourse(courseId, courseModel);
    }

    @GetMapping("/get/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getCourseById(@PathVariable("id") Long courseId) {
        return courseService.getCourseById(courseId);
    }
}

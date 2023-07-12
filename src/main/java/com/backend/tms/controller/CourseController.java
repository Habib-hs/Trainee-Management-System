package com.backend.tms.controller;

import com.backend.tms.model.Course.CourseReqModel;
import com.backend.tms.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping()
    public ResponseEntity<Object> createCourse(@RequestBody CourseReqModel courseModel) {
        return courseService.createCourse(courseModel);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteCourse(@PathVariable("id") Long courseId) {
        return courseService.deleteCourse(courseId);
    }

}
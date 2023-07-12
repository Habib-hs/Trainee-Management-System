package com.backend.tms.service;
import com.backend.tms.model.Course.CourseReqModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CourseService {

    ResponseEntity<Object> createCourse(CourseReqModel courseModel);
    ResponseEntity<Object> deleteCourse(Long courseId);
}
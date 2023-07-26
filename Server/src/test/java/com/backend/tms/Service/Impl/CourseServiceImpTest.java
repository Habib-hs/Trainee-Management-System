package com.backend.tms.Service.Impl;

import com.backend.tms.entity.CourseEntity;
import com.backend.tms.exception.custom.CourseNotFoundException;
import com.backend.tms.exception.custom.TrainerNotFoundException;
import com.backend.tms.model.Course.CourseReqModel;
import com.backend.tms.model.Course.CourseResModel;
import com.backend.tms.repository.CourseRepository;
import com.backend.tms.repository.TrainerRepository;
import com.backend.tms.service.Impl.CourseServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class CourseServiceImpTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CourseServiceImp courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCourse_WithNonExistingTrainerId_ShouldThrowTrainerNotFoundException() {
        // Arrange
        CourseReqModel courseReqModel = new CourseReqModel();
        // Populate courseReqModel with necessary data

        when(trainerRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TrainerNotFoundException.class, () -> courseService.createCourse(courseReqModel));
        verify(courseRepository, never()).save(any());
    }

    @Test
    void updateCourse_WithValidCourseIdAndData_ShouldReturnSuccessMessage() {
        // Arrange
        Long courseId = 1L;
        CourseReqModel courseReqModel = new CourseReqModel();
        // Populate courseReqModel with necessary data

        CourseEntity courseEntity = new CourseEntity();
        // Populate courseEntity with necessary data

        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(courseEntity));

        // Act
        ResponseEntity<Object> responseEntity = courseService.updateCourse(courseId, courseReqModel);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Course updated successfully", responseEntity.getBody());
        // Verify that courseEntity fields are updated correctly based on courseReqModel data
        verify(courseRepository, times(1)).save(courseEntity);
    }

    @Test
    void getCourseById_WithValidCourseId_ShouldReturnCourseResModel() {
        // Arrange
        Long courseId = 1L;
        CourseEntity courseEntity = new CourseEntity();
        // Populate courseEntity with necessary data

        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(courseEntity));
        when(modelMapper.map(any(), any())).thenReturn(new CourseResModel());

        // Act
        ResponseEntity<Object> responseEntity = courseService.getCourseById(courseId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void getCourseById_WithInvalidCourseId_ShouldThrowCourseNotFoundException() {
        // Arrange
        Long courseId = 1L;

        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CourseNotFoundException.class, () -> courseService.getCourseById(courseId));
    }

}

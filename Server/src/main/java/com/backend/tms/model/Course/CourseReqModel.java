package com.backend.tms.model.Course;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseReqModel {
    private Long id;
    private String name;
    private String description;
    private Long assignedTrainerId;
    private String trainerName;

    // Additional course-specific attributes and relationships can be added here

}
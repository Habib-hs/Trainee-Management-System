package com.backend.tms.model.Course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseResModel {
    private Long id;
    private String name;
    private String description;
    private Long assignedTrainerId;
    private String trainerName;
}

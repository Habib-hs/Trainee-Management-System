package com.backend.tms.model.Course;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseReqModel {
    private Long id;
    private String name;
    private String type;
    private String description;
    private Long assignedTrainerId;
    private String trainerName;
    private Date startDate;
    private Date endDate;

    // Additional course-specific attributes and relationships can be added here

}
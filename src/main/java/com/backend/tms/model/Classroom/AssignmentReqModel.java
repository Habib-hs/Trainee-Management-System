package com.backend.tms.model.Classroom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentReqModel {
    private Long id;
    private Long scheduleId;
    private Long trainerId;
    private String name;
    private String type;
    private Date deadline;
    private MultipartFile file;
    // Constructors, getters, and setters can be added here
}
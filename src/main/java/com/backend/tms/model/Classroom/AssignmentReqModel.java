package com.backend.tms.model.Classroom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentReqModel {
    private Long id;
    private Long scheduleId;
    private String name;
    private String type;
    private String File;
    private Timestamp deadline;

    // Constructors, getters, and setters can be added here
}
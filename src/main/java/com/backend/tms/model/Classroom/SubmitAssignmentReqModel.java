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
public class SubmitAssignmentReqModel {
    private Long id;
    private Long assignmentId;
    private Long traineeId;
    private String assignmentFile;
    private Timestamp time;
}
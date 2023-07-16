package com.backend.tms.model.Classroom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmitAssignmentReqModel {
    private Long id;
    private Long assignmentId;
    private Long traineeId;
    private Date time;
    private MultipartFile file;
}
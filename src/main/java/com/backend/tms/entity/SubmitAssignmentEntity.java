package com.backend.tms.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "submittedAssignment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmitAssignmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long assignmentId;
    private Long traineeId;
    private String assignmentFile;
    private Timestamp time;
}
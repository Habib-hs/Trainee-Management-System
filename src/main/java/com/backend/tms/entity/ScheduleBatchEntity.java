package com.backend.tms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "scheduleBatches")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleBatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String courseName;
    private String courseType;
    private Timestamp startDate;
    private Timestamp endDate;

    //relation with course
    @OneToOne
    private CourseEntity course;


    //relation with batch
    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "scheduleBatches_program",
            joinColumns = @JoinColumn(name = "programSchedule_id"),
            inverseJoinColumns = @JoinColumn(name = "batch_id")
    )
    private Set<BatchEntity> batches = new HashSet<>();

    //relation with assignment
    @Builder.Default
    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<AssignmentEntity> assignments = new HashSet<>();



// Constructors, getters, and setters can be added here

}
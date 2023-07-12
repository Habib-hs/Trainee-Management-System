package com.backend.tms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "schedulebatches")
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
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    //relation with course
    @OneToOne
    private CourseEntity course;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleBatchEntity that = (ScheduleBatchEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(courseName, that.courseName) &&
                Objects.equals(courseType, that.courseType) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, courseName, courseType, startDate, endDate);
    }

    //relation with batch
    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "schedulebatches_program",
            joinColumns = @JoinColumn(name = "programSchedule_id"),
            inverseJoinColumns = @JoinColumn(name = "batch_id")
    )
    private Set<BatchEntity> batches = new HashSet<>();

    //relation with assignment
    @Builder.Default
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<AssignmentEntity> assignments = new HashSet<>();

    // Constructors, getters, and setters can be added here
}

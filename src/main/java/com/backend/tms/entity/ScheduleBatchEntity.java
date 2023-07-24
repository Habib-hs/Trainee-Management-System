package com.backend.tms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
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
    private Date startDate;
    private Date endDate;

    //relation with course
    @OneToOne
    private CourseEntity course;


    //relation with batch
    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "schedulebatches_program",
            joinColumns = @JoinColumn(name = "programSchedule_id"),
            inverseJoinColumns = @JoinColumn(name = "batch_id")
    )
    @JsonIgnore
    private Set<BatchEntity> batches = new HashSet<>();

    //relation with assignment
    @Builder.Default
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<AssignmentEntity> assignments = new HashSet<>();

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

}

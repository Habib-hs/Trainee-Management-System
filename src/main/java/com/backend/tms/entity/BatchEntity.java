package com.backend.tms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "batches")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String batchName;
    private Timestamp startDate;
    private Timestamp endDate;

    // Relation with trainee
    @Builder.Default
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<TraineeEntity> trainees = new HashSet<>();

    // Relation with trainer
    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "batch_trainer",
            joinColumns = @JoinColumn(name = "batch_id"),
            inverseJoinColumns = @JoinColumn(name = "trainer_id")
    )
    private Set<TrainerEntity> trainers = new HashSet<>();

    // Relation with classroom
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private ClassroomEntity classroom;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BatchEntity that = (BatchEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Relation with batchSchedule
    @Builder.Default
    @ManyToMany(mappedBy = "batches")
    private Set<ScheduleBatchEntity> schedulePrograms = new HashSet<>();
}

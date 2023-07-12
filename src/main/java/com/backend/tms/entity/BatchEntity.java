package com.backend.tms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

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
    private Date startDate;
    private Date endDate;
    private int numberOfTrainee;

    //relation with classroom
    @OneToOne (fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private ClassroomEntity classroom;
}
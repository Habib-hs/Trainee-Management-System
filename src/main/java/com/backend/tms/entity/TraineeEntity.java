package com.backend.tms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "trainees")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class TraineeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String profilePicture;
    private String gender;
    private String dateOfBirth;
    private String email;
    private String password;
    private String domain;
    private String contactNumber;
    private String degreeName;
    private String educationalInstitute;
    private double cgpa;
    private int passingYear;
    private String presentAddress;

    @OneToOne (cascade = CascadeType.ALL)
    private UserEntity user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TraineeEntity that = (TraineeEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }



    // Additional attributes and relationships can be added here

}
package com.backend.tms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "trainers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String profilePicture;
    private String email;
    private String password;
    private String designation;
    private String joiningDate;
    private int yearsOfExperience;
    private String expertises;
    private String contactNumber;
    private String presentAddress;


    @OneToOne(cascade = CascadeType.ALL)
    private UserEntity user;
}
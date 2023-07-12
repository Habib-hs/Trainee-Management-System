package com.backend.tms.model.Trainee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TraineeReqModel {

    private Long id;
    private String fullName;
    private String profilePicture;
    private String gender;
    private String dateOfBirth;
    private String email;
    private String password;
    private String contactNumber;
    private String degreeName;
    private String educationalInstitute;
    private String domain;
    private double cgpa;
    private int passingYear;
    private String presentAddress;

    // Additional attributes and relationships can be added here

}
package com.backend.tms.model.Trainee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TraineeUpdateReqModel {
    private String fullName;
    private String profilePicture;
    private String gender;
    private String dateOfBirth;
    private String contactNumber;
    private String degreeName;
    private String educationalInstitute;
    private double cgpa;
    private int passingYear;
    private String presentAddress;
}

package com.backend.tms.model.Trainee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TraineeUpdateReqModel {
    private String fullName;
    private MultipartFile profilePicture;
    private String gender;
    private Date dateOfBirth;
    private String contactNumber;
    private String degreeName;
    private String educationalInstitute;
    private double cgpa;
    private int passingYear;
    private String presentAddress;
}

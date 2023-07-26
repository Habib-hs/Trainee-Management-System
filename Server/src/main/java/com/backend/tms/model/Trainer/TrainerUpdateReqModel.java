package com.backend.tms.model.Trainer;

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
public class TrainerUpdateReqModel {
    private String fullName;
    private MultipartFile profilePicture;
    private String designation;
    private Date joiningDate;
    private int yearsOfExperience;
    private String expertises;
    private String contactNumber;
    private String presentAddress;
}

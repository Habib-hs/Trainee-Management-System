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
    private String email;
    private String password;
    private String domain;
    // Additional attributes and relationships can be added here

}
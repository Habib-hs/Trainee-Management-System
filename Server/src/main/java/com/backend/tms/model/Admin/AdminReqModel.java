package com.backend.tms.model.Admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminReqModel {
    private Long id;
    private String fullName;
    private String gender;
    private String email;
    private String password;
    private String contactNumber;

    // Additional attributes and relationships can be added here

}
package com.backend.tms.model.Trainer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainerReqModel {
    private Long id;
    private String fullName;
    private String email;
    private String password;

}
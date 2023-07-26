package com.backend.tms.model.Trainer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddTrainerReqModel {
    private Long batchId;
    private List<Long> trainerIds;
}

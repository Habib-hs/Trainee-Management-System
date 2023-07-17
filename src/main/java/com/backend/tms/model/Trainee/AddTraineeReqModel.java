package com.backend.tms.model.Trainee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddTraineeReqModel {
        private Long batchId;
        private List<Long> traineeIds;

        // Add constructor, getters, and setters
}

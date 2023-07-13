package com.backend.tms.model.Batch;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatchReqModel {
    private Long id;
    private String batchName;
    private Timestamp startDate;
    private Timestamp endDate;
    private int numberOfTrainee;
}
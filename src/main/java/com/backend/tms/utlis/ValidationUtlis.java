package com.backend.tms.utlis;
import com.backend.tms.model.Batch.BatchReqModel;

import java.util.Date;
import java.util.concurrent.TimeUnit;


public class ValidationUtlis {

    public static boolean isBatchDurationValid(BatchReqModel batchModel) {
        Date startDate = batchModel.getStartDate();
        Date endDate = batchModel.getEndDate();

        // Calculate the difference in days between start date and end date
        long durationInDays = TimeUnit.MILLISECONDS.toDays(endDate.getTime() - startDate.getTime());

        // Check if the duration is between 90 days (3 months) and 124 days (4 months)
        return durationInDays >=90 && durationInDays <= 122;
    }

}

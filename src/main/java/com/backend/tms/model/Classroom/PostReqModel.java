package com.backend.tms.model.Classroom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostReqModel {
    private Long id;
    private String postTitle;
    private String postBody;
    private Long BatchId;
    private Long trainerId;
    private String attachment;
}
package com.backend.tms.model.Classroom;


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
public class CommentReqModel {
    private Long id;
    private String comment;
    private Long postId;
    private Timestamp time;
}
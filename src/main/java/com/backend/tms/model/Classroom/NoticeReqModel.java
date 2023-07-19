package com.backend.tms.model.Classroom;


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
public class NoticeReqModel {
    private Long id;
    private String NoticeTitle;
    private Long classroomId;
    private Long trainerId;
    private Date CreatedTime;
    private MultipartFile file;
}
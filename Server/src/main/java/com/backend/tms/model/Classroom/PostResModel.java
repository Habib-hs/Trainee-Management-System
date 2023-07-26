package com.backend.tms.model.Classroom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResModel {
    private Long id;
    private String title;
    private Date createdTime;
    private Long classroomId;
    private Long trainerId;
    private MultipartFile file;
    private String senderName;

}
package com.backend.tms.model.Classroom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResModel {
    private Long id;
    private String postTitle;
    private String postBody;
    private Long BatchId;
    private Long trainerId;
    private MultipartFile file;
}
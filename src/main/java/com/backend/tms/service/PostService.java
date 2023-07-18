package com.backend.tms.service;


import com.backend.tms.model.Classroom.PostReqModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface PostService {
    ResponseEntity<Object> createPost(PostReqModel postModel);
    ResponseEntity<Object> getPost(Long postId);
    ResponseEntity<Object> updatePost(Long postId, PostReqModel postModel);
    ResponseEntity<Object> downloadPostFile(Long postId);
}

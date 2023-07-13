package com.backend.tms.service;


import com.backend.tms.model.Classroom.PostReqModel;
import org.springframework.http.ResponseEntity;

public interface PostService {
    ResponseEntity<Object> createPost(PostReqModel postModel);
    ResponseEntity<Object> getPost(Long postId);
    ResponseEntity<Object> updatePost(Long postId, PostReqModel postModel);
}

package com.backend.tms.controller;

import com.backend.tms.model.Classroom.PostReqModel;
import com.backend.tms.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping()
    public ResponseEntity<Object> createPost(@ModelAttribute PostReqModel postModel) {
        return postService.createPost(postModel);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Object> getPost(@PathVariable("id") Long postId) {
        return postService.getPost(postId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePost(@PathVariable("id") Long postId, @RequestBody PostReqModel postModel) {
        return postService.updatePost(postId, postModel);
    }


}
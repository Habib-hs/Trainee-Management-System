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

    @PostMapping("/create")
    public ResponseEntity<Object> createPost(@RequestBody PostReqModel postModel) {
        return postService.createPost(postModel);
    }
}
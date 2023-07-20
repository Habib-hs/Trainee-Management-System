package com.backend.tms.controller;

import com.backend.tms.model.Classroom.PostMessageReqModel;
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

    @PostMapping("/message")
    public ResponseEntity<Object> createPostMessage(@RequestBody PostMessageReqModel postModel) {
        return postService.createPostMessage(postModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getPost(@PathVariable("id") Long postId) {
        return postService.getPost(postId);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<Object> updatePost(@PathVariable("id") Long postId, @ModelAttribute PostReqModel postModel) {
        return postService.updatePost(postId, postModel);
    }
    @GetMapping("/{id}/download")
    public ResponseEntity<Object> downloadPostFile(@PathVariable("id") Long postId) {
        return postService.downloadPostFile(postId);
    }

    @GetMapping("/classroom/{id}")
    public ResponseEntity<Object> getAllPostsByClassroom(@PathVariable("id") Long classroomId) {
        return postService.getAllPostsByClassroom(classroomId);
    }


}
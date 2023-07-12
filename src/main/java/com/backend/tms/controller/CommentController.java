package com.backend.tms.controller;

import com.backend.tms.model.Classroom.CommentReqModel;
import com.backend.tms.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}")
    public ResponseEntity<Object> createComment(@PathVariable("postId") Long postId, @RequestBody CommentReqModel commentModel) {
        return commentService.createComment(postId, commentModel);
    }

}
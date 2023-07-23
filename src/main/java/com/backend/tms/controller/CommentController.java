package com.backend.tms.controller;

import com.backend.tms.model.Classroom.CommentReqModel;
import com.backend.tms.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{postId}")
    @PreAuthorize("hasRole('TRAINEE')")
    public ResponseEntity<Object> createComment(@PathVariable("postId") Long postId, @RequestBody CommentReqModel commentModel) {
        return commentService.createComment(postId, commentModel);
    }

    @PutMapping("/{postId}")
    @PreAuthorize("hasRole('TRAINEE')")
    public ResponseEntity<Object> updateComment(@PathVariable("postId") Long postId, @RequestBody CommentReqModel commentModel) {
        return commentService.updateComment(postId, commentModel);
    }

}
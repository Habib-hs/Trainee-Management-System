package com.backend.tms.service.Impl;


import com.backend.tms.entity.CommentEntity;
import com.backend.tms.entity.PostEntity;
import com.backend.tms.model.Classroom.CommentReqModel;
import com.backend.tms.repository.CommentRepository;
import com.backend.tms.repository.PostRepository;
import com.backend.tms.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class CommentServiceImp implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<Object> createComment(Long postId, CommentReqModel commentModel) {

        PostEntity post = postRepository.findById(postId).orElse(null);

        CommentEntity commentEntity = modelMapper.map(commentModel,CommentEntity.class);
        CommentEntity createdComment = commentRepository.save(commentEntity);

        //add to the posts database
        post.getComments().add(createdComment);
        postRepository.save(post);

        if (createdComment != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Comment created successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create comment");
        }
    }

}
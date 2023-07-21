package com.backend.tms.service.Impl;


import com.backend.tms.entity.CommentEntity;
import com.backend.tms.entity.PostEntity;
import com.backend.tms.entity.TraineeEntity;
import com.backend.tms.exception.custom.CommentNotFoundException;
import com.backend.tms.exception.custom.PostNotFoundException;
import com.backend.tms.model.Classroom.CommentReqModel;
import com.backend.tms.repository.CommentRepository;
import com.backend.tms.repository.PostRepository;
import com.backend.tms.repository.TraineeRepository;
import com.backend.tms.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImp implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final TraineeRepository traineeRepository;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<Object> createComment(Long postId, CommentReqModel commentModel) {
        try {
            // Validate comment content
            if (commentModel.getComment() == null || commentModel.getComment().isEmpty()) {
                throw new IllegalArgumentException("Comment content is required");
            }
            // Check if the post exists
            PostEntity post = postRepository.findById(postId)
                    .orElseThrow(() -> new PostNotFoundException("Post not found"));

            // Create comment entity and save it
            CommentEntity commentEntity = modelMapper.map(commentModel, CommentEntity.class);
            //adding trainee name
            Optional<TraineeEntity> traineeEntity = traineeRepository.findById(commentModel.getTraineeId());
            if (traineeEntity.isPresent()) {
                TraineeEntity trainee = traineeEntity.get();
                commentEntity.setTraineeName(trainee.getFullName());
            }
            commentEntity.setPostId(postId);
            CommentEntity createdComment = commentRepository.save(commentEntity);

            // Add comment to the post
            post.getComments().add(createdComment);
            postRepository.save(post);

            return ResponseEntity.status(HttpStatus.CREATED).body("Comment created successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (PostNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create comment");
        }
    }
    @Override
    public ResponseEntity<Object> updateComment(Long commentId, CommentReqModel commentModel) {
        try {
            // Check if the comment exists
            CommentEntity existingComment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new CommentNotFoundException("Comment not found"));

            // Validate the comment content
            if (commentModel.getComment() == null || commentModel.getComment().isEmpty()) {
                throw new IllegalArgumentException("Comment content is required");
            }

            // Update the existing comment
            existingComment.setComment(commentModel.getComment());
           // existingComment.setUpdatedAt(new Date());
            CommentEntity updatedComment = commentRepository.save(existingComment);

            return ResponseEntity.status(HttpStatus.OK).body("Comment updated successfully");
        } catch (CommentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update comment");
        }
    }


}
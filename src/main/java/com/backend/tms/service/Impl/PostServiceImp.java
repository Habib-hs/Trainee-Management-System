package com.backend.tms.service.Impl;

import com.backend.tms.entity.BatchEntity;
import com.backend.tms.entity.PostEntity;
import com.backend.tms.entity.TrainerEntity;
import com.backend.tms.exception.custom.BatchNotFoundException;
import com.backend.tms.exception.custom.PostNotFoundException;
import com.backend.tms.exception.custom.TrainerNotFoundException;
import com.backend.tms.model.Classroom.PostReqModel;
import com.backend.tms.model.Classroom.PostResModel;
import com.backend.tms.repository.BatchRepository;
import com.backend.tms.repository.PostRepository;
import com.backend.tms.repository.TrainerRepository;
import com.backend.tms.service.PostService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImp implements PostService {
    private final PostRepository postRepository;
    private final BatchRepository batchRepository;
    private final TrainerRepository trainerRepository;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<Object> createPost(PostReqModel postModel) {
        // Validate if the associated batch exists
        BatchEntity batch = batchRepository.findById(postModel.getBatchId())
                .orElseThrow(() -> new BatchNotFoundException("Batch not found"));

        // Validate if the associated trainer exists
        TrainerEntity trainer = trainerRepository.findById(postModel.getTrainerId())
                .orElseThrow(() -> new TrainerNotFoundException("Trainer not found"));

        // Map PostReqModel to PostEntity
        PostEntity postEntity = modelMapper.map(postModel, PostEntity.class);

        // Save the post to the PostRepository
        PostEntity createdPost = postRepository.save(postEntity);

        // Add the created post to the classroom
        trainer.getPosts().add(createdPost);
        trainerRepository.save(trainer);

        return ResponseEntity.status(HttpStatus.CREATED).body("Post created successfully");
    }

    @Override
    public ResponseEntity<Object> getPost(Long postId) {
        try {
            PostEntity postEntity = postRepository.findById(postId)
                    .orElseThrow(() -> new PostNotFoundException("Post not found"));

            PostResModel postModel = modelMapper.map(postEntity, PostResModel.class);

            return ResponseEntity.ok(postModel);
        } catch (PostNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve post");
        }
    }

    @Override
    public ResponseEntity<Object> updatePost(Long postId, PostReqModel postModel) {
        try {
            PostEntity existingPost = postRepository.findById(postId)
                    .orElseThrow(() -> new PostNotFoundException("Post not found"));

            if (postModel.getPostTitle() == null || postModel.getPostTitle().isEmpty()) {
                throw new IllegalArgumentException("Post title is required");
            }
            if (postModel.getPostBody() == null || postModel.getPostBody().isEmpty()) {
                throw new IllegalArgumentException("Post body is required");
            }

            existingPost.setPostTitle(postModel.getPostTitle());
            existingPost.setPostBody(postModel.getPostBody());
            existingPost.setAttachment(postModel.getAttachment());

            postRepository.save(existingPost);

            return ResponseEntity.status(HttpStatus.OK).body("Post updated successfully");
        } catch (PostNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update post");
        }
    }

}

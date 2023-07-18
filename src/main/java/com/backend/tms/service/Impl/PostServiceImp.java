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
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PostServiceImp implements PostService {
    private final PostRepository postRepository;
    private final BatchRepository batchRepository;
    private final TrainerRepository trainerRepository;
    private final ModelMapper modelMapper;

    private static final String UPLOAD_DIR = "D:\\TMS FILE FOR POSTS";
    private static final String DOWNLOAD_DIR = "D:\\TMS FILE DOWNLOAD FOR POSTS";

    @Override
    public ResponseEntity<Object> createPost(PostReqModel postModel) {

        try{
            // Validate if the associated batch exists
            BatchEntity batchEntity = batchRepository.findById(postModel.getBatchId())
                    .orElseThrow(() -> new BatchNotFoundException("Batch not found"));

            // Validate if the associated trainer exists
            TrainerEntity trainerEntity = trainerRepository.findById(postModel.getTrainerId())
                    .orElseThrow(() -> new TrainerNotFoundException("Trainer not found"));

            MultipartFile file = postModel.getFile();
            String fileUrl=null;

            if (!file.isEmpty()) {
                fileUrl = uploadFile(file);
            }
            PostEntity postEntity = modelMapper.map(postModel, PostEntity.class);
            if (fileUrl != null) {
                postEntity.setFileUrl(fileUrl);
            }
            PostEntity createdPost = postRepository.save(postEntity);
            trainerEntity.getPosts().add(createdPost);
            return ResponseEntity.status(HttpStatus.CREATED).body("Post created successfully");

        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create assignment");
        }
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
           // existingPost.setFileUrl(postModel.getFile());

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

    private String uploadFile(MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            try {
                String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                File destinationDir = new File(UPLOAD_DIR);
                if (!destinationDir.exists()) {
                    destinationDir.mkdirs(); // Create the directory if it doesn't exist
                }
                File destinationFile = new File(destinationDir, fileName);
                file.transferTo(destinationFile);
                return destinationFile.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}

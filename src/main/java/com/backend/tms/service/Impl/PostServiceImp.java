package com.backend.tms.service.Impl;

import com.backend.tms.entity.BatchEntity;
import com.backend.tms.entity.ClassroomEntity;
import com.backend.tms.entity.PostEntity;
import com.backend.tms.entity.TrainerEntity;
import com.backend.tms.exception.custom.ClassroomNotFoundException;
import com.backend.tms.model.Classroom.PostMessageReqModel;
import com.backend.tms.repository.ClassroomRepository;
import org.apache.commons.io.FileUtils;
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
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class PostServiceImp implements PostService {
    private final PostRepository postRepository;
    private final BatchRepository batchRepository;
    private final TrainerRepository trainerRepository;
    private final ModelMapper modelMapper;
    private final ClassroomRepository classroomRepository;

    private static final String UPLOAD_DIR = "D:\\TMS FILE FOR POSTS";
    private static final String DOWNLOAD_DIR = "D:\\TMS FILE DOWNLOAD FOR POSTS";

    @Override
    public ResponseEntity<Object> createPost( PostReqModel postModel) {
        try {
           System.out.println("come inside");
            // Validate if the associated classroom exists
            ClassroomEntity classroomEntity = classroomRepository.findById(postModel.getClassroomId())
                    .orElseThrow(() -> new ClassroomNotFoundException("Classroom not found"));

            // Validate if the associated trainer exists
            TrainerEntity trainerEntity = trainerRepository.findById(postModel.getTrainerId())
                    .orElseThrow(() -> new TrainerNotFoundException("Trainer not found"));

            MultipartFile file = postModel.getFile();
            String fileUrl = null;

            if (file != null && !file.isEmpty()) {
                fileUrl = uploadFile(file);
            }
            PostEntity postEntity = modelMapper.map(postModel, PostEntity.class);
            if (fileUrl != null) {
                postEntity.setFileUrl(fileUrl);
            }
            Date currentTime = new Date();
            postEntity.setCreatedTime(currentTime);
            PostEntity createdPost = postRepository.save(postEntity);
            classroomEntity.getPosts().add(createdPost);
            classroomRepository.save(classroomEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body("Post created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create post");
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
            PostEntity postEntity = postRepository.findById(postId)
                    .orElseThrow(() -> new PostNotFoundException("Post not found with ID: " + postId));

            //updating the post Entity
            postEntity.setTitle(postModel.getTitle());
            postEntity.setBatchId(postModel.getClassroomId());
            postEntity.setTrainerId(postModel.getTrainerId());

            MultipartFile file = postModel.getFile();
            if (file != null && !file.isEmpty()) {
                String fileUrl = uploadFile(file);
                if (fileUrl == null) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload the file");
                }
                postEntity.setFileUrl(fileUrl);
            }
            postRepository.save(postEntity);
            return ResponseEntity.status(HttpStatus.OK).body("Post updated successfully");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update post");
        }
    }

    @Override
    public ResponseEntity<Object> downloadPostFile(Long postId) {
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(()->new PostNotFoundException("post not found"));
       // if(postEntity.getFileUrl()==null){throw FileNotFoundException("File not found for download")}
       // }
       try{
           File file = new File(postEntity.getFileUrl());
           String fileContents = FileUtils.readFileToString(file, StandardCharsets.UTF_8);

           // Create a new file in the specified directory
           String fileName = StringUtils.cleanPath(file.getName());
           File destinationDir = new File(DOWNLOAD_DIR);
           if (!destinationDir.exists()) {
               destinationDir.mkdirs(); // Create the directory if it doesn't exist
           }
           File destinationFile = new File(destinationDir, fileName);
           FileUtils.writeStringToFile(destinationFile, fileContents, StandardCharsets.UTF_8);
           String message = "Download successful. File saved to: " + destinationFile.getAbsolutePath();
           return ResponseEntity.ok(message);
       }catch(Exception e){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to read or save the file");
        }
    }

    @Override
    public ResponseEntity<Object> createPostMessage(PostMessageReqModel postModel) {
        // Validate if the associated batch exists
        BatchEntity batchEntity = batchRepository.findById(postModel.getBatchId())
                .orElseThrow(() -> new BatchNotFoundException("Batch not found"));

        // Validate if the associated trainer exists
        TrainerEntity trainerEntity = trainerRepository.findById(postModel.getTrainerId())
                .orElseThrow(() -> new TrainerNotFoundException("Trainer not found"));

        PostEntity postEntity = modelMapper.map(postModel, PostEntity.class);
        PostEntity createdPost = postRepository.save(postEntity);
       // trainerEntity.getPosts().add(createdPost);
        return ResponseEntity.status(HttpStatus.CREATED).body("Post created successfully");
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

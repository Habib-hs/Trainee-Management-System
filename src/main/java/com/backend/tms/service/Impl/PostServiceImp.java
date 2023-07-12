package com.backend.tms.service.Impl;


import com.backend.tms.entity.ClassroomEntity;
import com.backend.tms.entity.PostEntity;
import com.backend.tms.model.Classroom.PostReqModel;
import com.backend.tms.repository.ClassroomRepository;
import com.backend.tms.repository.PostRepository;
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
    private final ClassroomRepository classroomRepository;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<Object> createPost(PostReqModel postModel) {

        // Save the post to the PostRepository
        PostEntity postEntity = modelMapper.map(postModel,PostEntity.class );
        PostEntity createdPost = postRepository.save(postEntity);

        if (createdPost != null) {
            // Associate the post with a classroom
            ClassroomEntity classroom = classroomRepository.findById(postModel.getBatchId()).orElse(null);

            // Add the created post to the classroom
            classroom.getPosts().add(createdPost);
            classroomRepository.save(classroom);
            return ResponseEntity.status(HttpStatus.CREATED).body("Post created successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create post");
        }
    }
}
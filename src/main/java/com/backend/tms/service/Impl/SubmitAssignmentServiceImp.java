package com.backend.tms.service.Impl;

import com.backend.tms.entity.AssignmentEntity;
import com.backend.tms.entity.SubmitAssignmentEntity;
import com.backend.tms.model.Classroom.SubmitAssignmentReqModel;
import com.backend.tms.repository.AssignmentRepository;
import com.backend.tms.repository.SubmitAssignmentRepository;
import com.backend.tms.service.SubmitAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class SubmitAssignmentServiceImp implements SubmitAssignmentService {

    private final SubmitAssignmentRepository submitAssignmentRepository;
    private final AssignmentRepository assignmentRepository;

    @Override
    public ResponseEntity<Object> submitAssignment(Long assignmentId, SubmitAssignmentReqModel submitAssignmentModel) {

        // Save the submitted assignment to the submitAssignmentRepository
        SubmitAssignmentEntity submitAssignmentEntity = SubmitAssignmentEntity.builder()
                .assignmentId(assignmentId)
                .traineeId(submitAssignmentModel.getTraineeId())
                .assignmentFile(submitAssignmentModel.getAssignmentFile())
                .time(submitAssignmentModel.getTime())
                .build();
        SubmitAssignmentEntity savedSubmitAssignment = submitAssignmentRepository.save(submitAssignmentEntity);


        // Update the assignment with the submission details
        Optional<AssignmentEntity> assignment = assignmentRepository.findById(assignmentId);
        if (assignment.isPresent()) {
            AssignmentEntity assignmentEntity = assignment.get();
            assignmentEntity.getSubAssignments().add(savedSubmitAssignment);
            assignmentRepository.save(assignmentEntity);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Assignment not found");
        }

        // Return a success response
        return ResponseEntity.status(HttpStatus.CREATED).body("Assignment submitted successfully");
    }

}
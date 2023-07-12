package com.backend.tms.service.Impl;

import com.backend.tms.entity.AssignmentEntity;
import com.backend.tms.entity.ScheduleBatchEntity;
import com.backend.tms.model.Classroom.AssignmentReqModel;
import com.backend.tms.repository.AssignmentRepository;
import com.backend.tms.repository.ScheduleRepository;
import com.backend.tms.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImp implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final ScheduleRepository scheduleRepository;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<Object> createAssignment(AssignmentReqModel assignmentModel) {
        System.out.println("come to the create assignmetn");
        AssignmentEntity assignmentEntity = modelMapper.map(assignmentModel, AssignmentEntity.class);
        AssignmentEntity createdAssignment = assignmentRepository.save(assignmentEntity);

        // Add assignment to the corresponding ScheduleBatchEntity
        if (createdAssignment != null) {
            ScheduleBatchEntity scheduleBatchEntity = scheduleRepository
                    .findById(assignmentModel.getScheduleId()).orElse(null);

            if (scheduleBatchEntity != null) {
                scheduleBatchEntity.getAssignments().add(createdAssignment);
                scheduleRepository.save(scheduleBatchEntity);
                return ResponseEntity.status(HttpStatus.CREATED).body("Assignment created successfully");
            }
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create assignment");
    }
}
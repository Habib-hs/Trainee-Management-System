package com.backend.tms.service.Impl;
import com.backend.tms.entity.AssignmentEntity;
import com.backend.tms.entity.ClassroomEntity;
import com.backend.tms.entity.ScheduleBatchEntity;
import com.backend.tms.exception.custom.AssignmentNotFoundException;
import com.backend.tms.exception.custom.ScheduleNotFoundException;
import com.backend.tms.model.Classroom.AssignmentReqModel;
import com.backend.tms.model.Classroom.AssignmentResModel;
import com.backend.tms.repository.AssignmentRepository;
import com.backend.tms.repository.ClassroomRepository;
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
        try {
            // Map AssignmentReqModel to AssignmentEntity
            AssignmentEntity assignmentEntity = modelMapper.map(assignmentModel, AssignmentEntity.class);

            // Add assignment to the corresponding ScheduleBatchEntity
            ScheduleBatchEntity scheduleBatchEntity = scheduleRepository
                    .findById(assignmentModel.getScheduleId())
                    .orElseThrow(() -> new ScheduleNotFoundException("Schedule not found"));

            scheduleBatchEntity.getAssignments().add(assignmentEntity);
            scheduleRepository.save(scheduleBatchEntity);

            return ResponseEntity.status(HttpStatus.CREATED).body("Assignment created successfully");
        } catch (ScheduleNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create assignment");
        }
    }

    @Override
    public ResponseEntity<Object> getAssignment(Long assignmentId) {
        try {
            AssignmentEntity assignmentEntity = assignmentRepository.findById(assignmentId)
                    .orElseThrow(() -> new AssignmentNotFoundException("Assignment not found"));

            AssignmentResModel assignmentModel = modelMapper.map(assignmentEntity, AssignmentResModel.class);

            return ResponseEntity.status(HttpStatus.OK).body(assignmentModel);
        } catch (AssignmentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve assignment");
        }
    }

    @Override
    public ResponseEntity<Object> updateAssignment(Long assignmentId, AssignmentReqModel assignmentModel) {
        try {
            AssignmentEntity assignmentEntity = assignmentRepository.findById(assignmentId)
                    .orElseThrow(() -> new AssignmentNotFoundException("Assignment not found"));

            assignmentEntity.setName(assignmentModel.getName());
            assignmentEntity.setType(assignmentModel.getType());
            assignmentEntity.setFile(assignmentModel.getFile());
            assignmentEntity.setDeadline(assignmentModel.getDeadline());

            assignmentRepository.save(assignmentEntity);

            return ResponseEntity.status(HttpStatus.OK).body("Assignment updated successfully");
        } catch (AssignmentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update assignment");
        }
    }

    @Override
    public ResponseEntity<Object> deleteAssignment(Long assignmentId) {
        try {
            AssignmentEntity assignmentEntity = assignmentRepository.findById(assignmentId)
                    .orElseThrow(() -> new AssignmentNotFoundException("Assignment not found"));

            // Remove the assignment from the associated ClassroomEntity
            ScheduleBatchEntity scheduleEntity = scheduleRepository.findByAssignmentsContaining(assignmentEntity);
            if (scheduleEntity != null) {
                scheduleEntity.getAssignments().remove(assignmentEntity);
                scheduleRepository.save(scheduleEntity);
            }

            // Delete the assignment
            assignmentRepository.delete(assignmentEntity);

            return ResponseEntity.status(HttpStatus.OK).body("Assignment deleted successfully");
        } catch (AssignmentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete assignment");
        }
    }

}

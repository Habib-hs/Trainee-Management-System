package com.backend.tms.service.Impl;
import java.io.File;
import java.nio.charset.StandardCharsets;

import com.backend.tms.entity.*;
import com.backend.tms.exception.custom.AssignmentNotFoundException;
import com.backend.tms.exception.custom.ScheduleNotFoundException;
import com.backend.tms.exception.custom.TrainerNotFoundException;
import com.backend.tms.model.Classroom.AssignmentReqModel;
import com.backend.tms.model.Classroom.AssignmentResModel;
import com.backend.tms.repository.AssignmentRepository;
import com.backend.tms.repository.ScheduleRepository;
import com.backend.tms.repository.TrainerRepository;
import com.backend.tms.service.AssignmentService;
import com.backend.tms.utlis.AppConstants;
import com.backend.tms.utlis.FileService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AssignmentServiceImp implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final ScheduleRepository scheduleRepository;
    private final TrainerRepository trainerRepository;
    private final ModelMapper modelMapper;


    public ResponseEntity<Object> createAssignment(AssignmentReqModel assignmentModel) {
        try {
            MultipartFile file = assignmentModel.getFile();
            String fileUrl = null;
            if (file != null && !file.isEmpty()) {
                fileUrl = FileService.uploadFile(file, AppConstants.ASSIGNMENT_UPLOAD_DIR );
            }
            AssignmentEntity assignmentEntity = modelMapper.map(assignmentModel, AssignmentEntity.class);
            if (fileUrl != null) {
                assignmentEntity.setFileUrl(fileUrl);
            }
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

    public ResponseEntity<Object> updateAssignment(Long assignmentId, AssignmentReqModel assignmentModel) {
        try {
            AssignmentEntity assignmentEntity = assignmentRepository.findById(assignmentId)
                    .orElseThrow(() -> new AssignmentNotFoundException("Assignment not found"));

            updateAssignmentAttributes(assignmentEntity, assignmentModel);

            MultipartFile file = assignmentModel.getFile();
            if (file != null && !file.isEmpty()) {
                String fileUrl = AppConstants.ASSIGNMENT_UPLOAD_DIR ;
                if (fileUrl == null) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload the file");
                }
                assignmentEntity.setFileUrl(fileUrl);
            }
            assignmentRepository.save(assignmentEntity);

            return ResponseEntity.status(HttpStatus.OK).body("Assignment updated successfully");
        } catch (AssignmentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update assignment");
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
    public ResponseEntity<Object> downloadAssignmentFile(Long assignmentId) {
        AssignmentEntity assignmentEntity = assignmentRepository.getById(assignmentId);
        if (assignmentEntity == null || assignmentEntity.getFileUrl() == null) {
            return ResponseEntity.notFound().build();
        }
        // Perform any necessary operations with the assignment file
        try {
            File file = new File(assignmentEntity.getFileUrl());
            String fileContents = FileUtils.readFileToString(file, StandardCharsets.UTF_8);

            // Create a new file in the specified directory
            String fileName = StringUtils.cleanPath(file.getName());
            File destinationDir = new File(AppConstants.ASSIGNMENT_DOWNLOAD_DIR);
            if (!destinationDir.exists()) {
                destinationDir.mkdirs(); // Create the directory if it doesn't exist
            }
            File destinationFile = new File(destinationDir, fileName);
            FileUtils.writeStringToFile(destinationFile, fileContents, StandardCharsets.UTF_8);
            String message = "Download successful. File saved to: " + destinationFile.getAbsolutePath();
            return ResponseEntity.ok(message);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to read or save the file");
        }
    }

    @Override
    public ResponseEntity<Object> getAllAssignmentsWithoutSubmittedList() {
        List<AssignmentEntity> assignments = assignmentRepository.findAll();

        Map<String, Object> response = new HashMap<>();
        response.put("Total Assignment", assignments.size());

        List<Map<String, Object>> assignmentList = new ArrayList<>();
        for (AssignmentEntity assignmentEntity : assignments) {
            Map<String, Object> assignmentMap = new HashMap<>();
            assignmentMap.put("id", assignmentEntity.getId());
            assignmentMap.put("name", assignmentEntity.getName());
            assignmentMap.put("type", assignmentEntity.getType());
            assignmentMap.put("deadline", assignmentEntity.getDeadline());
            assignmentMap.put("fileUrl", assignmentEntity.getFileUrl());
            assignmentMap.put("description", assignmentEntity.getDescription());
            // Fetch the scheduleEntity from the scheduleRepository using the scheduleId
            ScheduleBatchEntity scheduleEntity = scheduleRepository.findById(assignmentEntity.getScheduleId())
                    .orElseThrow(() -> new ScheduleNotFoundException("Schedule not found for assignment with ID: " + assignmentEntity.getId()));
            assignmentMap.put("scheduleName", scheduleEntity.getCourseName());

            // fetch the batches that is related to schedule
            Set<BatchEntity>  batchEntities= scheduleEntity.getBatches();
                    List<Long> batchIds = batchEntities.stream()
                    .map(BatchEntity::getId)
                    .collect(Collectors.toList());
            assignmentMap.put("batches" , batchIds) ;

            assignmentList.add(assignmentMap);
        }
        response.put("Assignments", assignmentList);

        return new ResponseEntity<>(response, HttpStatus.OK);


    }

    @Override
    public ResponseEntity<Object> getAssignmentListByTrainer(Long trainerId) {
        TrainerEntity trainerEntity = trainerRepository.findById(trainerId).orElseThrow(()->new TrainerNotFoundException("trainer not found!"));
        List<AssignmentEntity> assignments = assignmentRepository.findByTrainerId(trainerId);

        Map<String, Object> response = new HashMap<>();
        response.put("Total Assignment", assignments.size());
        List<Map<String, Object>> assignmentList = new ArrayList<>();
        for (AssignmentEntity assignmentEntity : assignments) {
            Map<String, Object> assignmentMap = new HashMap<>();
            assignmentMap.put("id", assignmentEntity.getId());
            assignmentMap.put("name", assignmentEntity.getName());
            assignmentMap.put("type", assignmentEntity.getType());
            assignmentMap.put("deadline", assignmentEntity.getDeadline());
            assignmentMap.put("description", assignmentEntity.getDescription());
            assignmentMap.put("fileUrl", assignmentEntity.getFileUrl());

            // Fetch the scheduleEntity from the scheduleRepository using the scheduleId
            ScheduleBatchEntity scheduleEntity = scheduleRepository.findById(assignmentEntity.getScheduleId())
                    .orElseThrow(() -> new ScheduleNotFoundException("Schedule not found for assignment with ID: " + assignmentEntity.getId()));
            assignmentMap.put("scheduleName", scheduleEntity.getCourseName());

            //fetch the total submitted assignment
             assignmentMap.put("totalSubmitted", assignmentEntity.getSubAssignments().size());
             assignmentMap.put("submittedAssignment", assignmentEntity.getSubAssignments());
            assignmentList.add(assignmentMap);
            }
        response.put("Assignments", assignmentList);

        return new ResponseEntity<>(response, HttpStatus.OK);
        }


    private void updateAssignmentAttributes(AssignmentEntity assignmentEntity, AssignmentReqModel assignmentModel) {
       assignmentEntity.setScheduleId(assignmentModel.getScheduleId());
        assignmentEntity.setName(assignmentModel.getName());
        assignmentEntity.setType(assignmentModel.getType());
        assignmentEntity.setDeadline(assignmentModel.getDeadline());
    }




}

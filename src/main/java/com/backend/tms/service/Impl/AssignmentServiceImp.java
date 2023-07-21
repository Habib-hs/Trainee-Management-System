package com.backend.tms.service.Impl;
import java.io.File;
import java.nio.charset.StandardCharsets;

import com.backend.tms.entity.AssignmentEntity;
import com.backend.tms.entity.ScheduleBatchEntity;
import com.backend.tms.exception.custom.AssignmentNotFoundException;
import com.backend.tms.exception.custom.ScheduleNotFoundException;
import com.backend.tms.model.Classroom.AssignmentReqModel;
import com.backend.tms.model.Classroom.AssignmentResModel;
import com.backend.tms.repository.AssignmentRepository;
import com.backend.tms.repository.ScheduleRepository;
import com.backend.tms.service.AssignmentService;
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


@Service
@RequiredArgsConstructor
public class AssignmentServiceImp implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final ScheduleRepository scheduleRepository;
    private final ModelMapper modelMapper;
    private static final String UPLOAD_DIR = "D:\\TMS FILE";
    private static final String DOWNLOAD_DIR = "D:\\TMS FILE DOWNLOAD";

    public ResponseEntity<Object> createAssignment(AssignmentReqModel assignmentModel) {
        try {
            MultipartFile file = assignmentModel.getFile();
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("No file selected.");
            }

            String fileUrl = uploadFile(file);
            if (fileUrl == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload the file");
            }

            AssignmentEntity assignmentEntity = modelMapper.map(assignmentModel, AssignmentEntity.class);
            assignmentEntity.setFileUrl(fileUrl);

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
                String fileUrl = uploadFile(file);
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
            File destinationDir = new File(DOWNLOAD_DIR);
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

            // Fetch the scheduleEntity from the scheduleRepository using the scheduleId
            ScheduleBatchEntity scheduleEntity = scheduleRepository.findById(assignmentEntity.getScheduleId())
                    .orElseThrow(() -> new ScheduleNotFoundException("Schedule not found for assignment with ID: " + assignmentEntity.getId()));
            assignmentMap.put("scheduleName", scheduleEntity.getCourseName());
            assignmentList.add(assignmentMap);
        }

        response.put("Assignments", assignmentList);

        return new ResponseEntity<>(response, HttpStatus.OK);


    }

    private void updateAssignmentAttributes(AssignmentEntity assignmentEntity, AssignmentReqModel assignmentModel) {
        assignmentEntity.setName(assignmentModel.getName());
        assignmentEntity.setType(assignmentModel.getType());
        assignmentEntity.setDeadline(assignmentModel.getDeadline());
        // Update other assignment attributes as needed
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

package com.backend.tms.service.Impl;

import com.backend.tms.entity.AssignmentEntity;
import com.backend.tms.entity.SubmitAssignmentEntity;
import com.backend.tms.exception.custom.AssignmentNotFoundException;
import com.backend.tms.model.Classroom.SubmitAssignmentReqModel;
import com.backend.tms.repository.AssignmentRepository;
import com.backend.tms.repository.SubmitAssignmentRepository;
import com.backend.tms.service.SubmitAssignmentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class SubmitAssignmentServiceImp implements SubmitAssignmentService {

    private final SubmitAssignmentRepository submitAssignmentRepository;
    private final AssignmentRepository assignmentRepository;
    private final ModelMapper modelMapper;

    private static final String UPLOAD_DIR = "D:\\TMS FILE";
    private static final String DOWNLOAD_DIR = "D:\\TMS FILE DOWNLOAD";

    @Override
    public ResponseEntity<Object> submitAssignment(Long assignmentId, SubmitAssignmentReqModel submitAssignmentModel) {

        try {
            MultipartFile file = submitAssignmentModel.getFile();
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("No file selected.");
            }
            String fileUrl = uploadFile(file);
            if (fileUrl == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload the file");
            }

            //saved the submitted assignment
            SubmitAssignmentEntity subAssignmentEntity = modelMapper.map(submitAssignmentModel, SubmitAssignmentEntity.class);
            subAssignmentEntity.setSubmitFileUrl(fileUrl);
            submitAssignmentRepository.save(subAssignmentEntity);

            // Update the assignment with the submission details
            Optional<AssignmentEntity> assignment = assignmentRepository.findById(assignmentId);
            if (assignment.isPresent()) {
                AssignmentEntity assignmentEntity = assignment.get();
                assignmentEntity.getSubAssignments().add(subAssignmentEntity);
                assignmentRepository.save(assignmentEntity);
            } else {
                throw new AssignmentNotFoundException("Assignment Not Found!");
            }
            // Return a success response
            return ResponseEntity.status(HttpStatus.CREATED).body("Assignment submitted successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to submit the assignment");
        }
    }

    @Override
    public ResponseEntity<Object> updateAssignment(Long subAssignmentId, SubmitAssignmentReqModel submitAssignmentModel) {
        try {
            MultipartFile file = submitAssignmentModel.getFile();
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("No file selected.");
            }
            String fileUrl = uploadFile(file);
            if (fileUrl == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload the file");
            }

            // Retrieve the existing submitted assignment
            Optional<SubmitAssignmentEntity> submittedAssignment = submitAssignmentRepository.findById(subAssignmentId);
            if (submittedAssignment.isPresent()) {
                SubmitAssignmentEntity subAssignmentEntity = submittedAssignment.get();
                subAssignmentEntity.setAssignmentId(subAssignmentEntity.getAssignmentId());
                subAssignmentEntity.setTraineeId(subAssignmentEntity.getTraineeId());
                subAssignmentEntity.setSubmitFileUrl(fileUrl);
                subAssignmentEntity.setTime(submitAssignmentModel.getTime());
                submitAssignmentRepository.save(subAssignmentEntity);
                // Return a success response
                return ResponseEntity.ok("Assignment updated successfully");
            }else {
                throw new AssignmentNotFoundException("Assignment not found with ID: " + subAssignmentId);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update the assignment");
        }
    }

    @Override
    public ResponseEntity<Object> downloadAssignment(Long assignmentId) {
        return null;
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
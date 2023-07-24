package com.backend.tms.service.Impl;

import com.backend.tms.entity.AssignmentEntity;
import com.backend.tms.entity.NoticeEntity;
import com.backend.tms.entity.SubmitAssignmentEntity;
import com.backend.tms.entity.TraineeEntity;
import com.backend.tms.exception.custom.AssignmentNotFoundException;
import com.backend.tms.exception.custom.FileNotFoundException;
import com.backend.tms.exception.custom.NoticeNotFoundException;
import com.backend.tms.exception.custom.SubmittedAssignmentNotFound;
import com.backend.tms.model.Classroom.SubmitAssignmentReqModel;
import com.backend.tms.repository.AssignmentRepository;
import com.backend.tms.repository.SubmitAssignmentRepository;
import com.backend.tms.repository.TraineeRepository;
import com.backend.tms.service.SubmitAssignmentService;
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

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
public class SubmitAssignmentServiceImp implements SubmitAssignmentService {

    private final SubmitAssignmentRepository submitAssignmentRepository;
    private final AssignmentRepository assignmentRepository;
    private final TraineeRepository traineeRepository;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<Object> submitAssignment(Long assignmentId, SubmitAssignmentReqModel submitAssignmentModel) {
        try {

            Optional<AssignmentEntity> assignment = assignmentRepository.findById(assignmentId);
            if(!assignment.isPresent()){
                throw new AssignmentNotFoundException("Assignment Not Found!");
            }

            MultipartFile file = submitAssignmentModel.getFile();
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file is selected");
            }
            String fileUrl =  FileService.uploadFile(file, AppConstants.SUBMIT_ASSIGNMENT_UPLOAD_DIR );
            if (fileUrl == null ) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload the file");
            }

            //getting the traineeName
            Optional<TraineeEntity> traineeEntity = traineeRepository.findById(submitAssignmentModel.getTraineeId());
             TraineeEntity trainee = traineeEntity.get();

            //saved the submitted assignment
            SubmitAssignmentEntity subAssignmentEntity = modelMapper.map(submitAssignmentModel, SubmitAssignmentEntity.class);
            subAssignmentEntity.setSubmitFileUrl(fileUrl);
            Date currentTime = new Date();
            subAssignmentEntity.setCreatedTime(currentTime);
            subAssignmentEntity.setTraineeName(trainee.getFullName());
            Date deadline = assignment.get().getDeadline();

            // Calculate the submission status based on the deadline and current time
            subAssignmentEntity.setSubmittedStatus(calculateSubmissionStatus(currentTime, deadline));
            submitAssignmentRepository.save(subAssignmentEntity);

            // Update the relationship between AssignmentEntity and SubmitAssignmentEntity
            AssignmentEntity assignmentEntity = assignment.get();
            assignmentEntity.getSubAssignments().add(subAssignmentEntity);
            assignmentRepository.save(assignmentEntity);

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
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file is selected");
            }
            String fileUrl =  FileService.uploadFile(file, AppConstants.ASSIGNMENT_UPLOAD_DIR );
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
                submitAssignmentRepository.save(subAssignmentEntity);
                return ResponseEntity.ok("Assignment updated successfully");
            }else {
                throw new AssignmentNotFoundException("Assignment not found with ID: " + subAssignmentId);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update the assignment");
        }
    }

    @Override
    public ResponseEntity<Object> downloadSubAssignment(Long subAssignmentId) {
        SubmitAssignmentEntity subAssignmentEntity = submitAssignmentRepository.findById(subAssignmentId).orElseThrow(() -> new SubmittedAssignmentNotFound("submitted Assignment not found with ID: " + subAssignmentId));
        if(subAssignmentEntity.getSubmitFileUrl()==null){throw new FileNotFoundException("File not found for download");}

        try{
            File file = new File(subAssignmentEntity.getSubmitFileUrl());
            String fileContents = FileUtils.readFileToString(file, StandardCharsets.UTF_8);

            // Create a new file in the specified directory
            String fileName = StringUtils.cleanPath(file.getName());
            File destinationDir = new File(AppConstants.SUBMIT_ASSIGNMENT_DOWNLOAD_DIR);
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


    private String calculateSubmissionStatus(Date currentTime, Date deadline) {
        if (currentTime.after(deadline)) {
            long delayInMillis = currentTime.getTime() - deadline.getTime();
            long delayInMinutes = TimeUnit.MILLISECONDS.toMinutes(delayInMillis);
            long delayInHours = TimeUnit.MILLISECONDS.toHours(delayInMillis);
            long delayInDays = TimeUnit.MILLISECONDS.toDays(delayInMillis);

            // Set the delayed status message based on the time difference
            String delayedStatus = "Delayed ";
            if (delayInDays > 0) {
                delayedStatus += delayInDays + " days";
            } else if (delayInHours > 0) {
                delayedStatus += delayInHours + " hours";
            } else {
                delayedStatus += delayInMinutes + " minutes";
            }

            return delayedStatus;
        } else {
            // If current time is on or before the deadline, set the submission status as in-time
            return "In-time";
        }
    }

    }
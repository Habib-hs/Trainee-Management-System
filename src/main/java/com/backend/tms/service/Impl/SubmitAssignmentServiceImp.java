package com.backend.tms.service.Impl;

import com.backend.tms.entity.AssignmentEntity;
import com.backend.tms.entity.SubmitAssignmentEntity;
import com.backend.tms.entity.TraineeEntity;
import com.backend.tms.exception.custom.AssignmentNotFoundException;
import com.backend.tms.model.Classroom.SubmitAssignmentReqModel;
import com.backend.tms.repository.AssignmentRepository;
import com.backend.tms.repository.SubmitAssignmentRepository;
import com.backend.tms.repository.TraineeRepository;
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

            //getting the traineeName
            Optional<TraineeEntity> traineeEntity = traineeRepository.findById(submitAssignmentModel.getTraineeId());
             TraineeEntity trainee = traineeEntity.get();

            //saved the submitted assignment
            SubmitAssignmentEntity subAssignmentEntity = modelMapper.map(submitAssignmentModel, SubmitAssignmentEntity.class);
            subAssignmentEntity.setSubmitFileUrl(fileUrl);
            Date currentTime = new Date();
            subAssignmentEntity.setCreatedTime(currentTime);
            subAssignmentEntity.setTraineeName(trainee.getFullName());

            // Update the assignment with the submission details
            Optional<AssignmentEntity> assignment = assignmentRepository.findById(assignmentId);
            if(!assignment.isPresent()){
                throw new AssignmentNotFoundException("Assignment Not Found!");
            }

            // Fetch the assignment deadline from the AssignmentEntity
            Date deadline = assignment.get().getDeadline();


            // Compare the deadline with the current time
            if (currentTime.after(deadline)) {
                // If current time is after the deadline, set the submission status as delayed
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

                subAssignmentEntity.setSubmittedStatus(delayedStatus);
            } else {
                // If current time is on or before the deadline, set the submission status as in-time
                subAssignmentEntity.setSubmittedStatus("In-time");
            }

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
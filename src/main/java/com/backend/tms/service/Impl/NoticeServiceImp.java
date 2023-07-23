package com.backend.tms.service.Impl;

import com.backend.tms.entity.ClassroomEntity;
import com.backend.tms.entity.NoticeEntity;
import com.backend.tms.entity.PostEntity;
import com.backend.tms.entity.TrainerEntity;
import com.backend.tms.exception.custom.*;
import com.backend.tms.model.Classroom.NoticeReqModel;
import com.backend.tms.model.Classroom.NoticeResModel;
import com.backend.tms.repository.BatchRepository;
import com.backend.tms.repository.ClassroomRepository;
import com.backend.tms.repository.NoticeRepository;
import com.backend.tms.repository.TrainerRepository;
import com.backend.tms.service.NoticeService;
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
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
public class NoticeServiceImp implements NoticeService {
    private final NoticeRepository noticeRepository;
    private final BatchRepository batchRepository;
    private final ModelMapper modelMapper;
    private final TrainerRepository trainerRepository;
    private final ClassroomRepository classroomRepository;


    @Override
    public ResponseEntity<Object> createNotice(NoticeReqModel noticeReqModel) {
        try {
            // Validate if the associated classroom exists
            ClassroomEntity classroomEntity = classroomRepository.findById(noticeReqModel.getClassroomId())
                    .orElseThrow(() -> new ClassroomNotFoundException("Classroom not found"));

            // Validate if the associated trainer exists
            TrainerEntity trainerEntity = trainerRepository.findById(noticeReqModel.getTrainerId())
                    .orElseThrow(() -> new TrainerNotFoundException("Trainer not found"));

            MultipartFile file = noticeReqModel.getFile();
            String fileUrl = null;

            if (file != null && !file.isEmpty()) {
                fileUrl = FileService.uploadFile(file, AppConstants.NOTICE_UPLOAD_DIR);
            }

            NoticeEntity noticeEntity = modelMapper.map(noticeReqModel, NoticeEntity.class);
            Date currentTime = new Date();
            noticeEntity.setCreatedTime(currentTime);
            if (fileUrl != null) {
                noticeEntity.setFileUrl(fileUrl);
            }
            NoticeEntity createdNotice = noticeRepository.save(noticeEntity);
            classroomEntity.getNotices().add(createdNotice);
            classroomRepository.save(classroomEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body("Notice created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create Notice");
        }
    }



    @Override
    public ResponseEntity<Object> getNotice(Long noticeId) {
        try {
            NoticeEntity noticeEntity = noticeRepository.findById(noticeId)
                    .orElseThrow(() -> new NoticeNotFoundException("notice not found"));

            NoticeResModel noticeModel = modelMapper.map(noticeEntity, NoticeResModel.class);
            return ResponseEntity.ok(noticeModel);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve post");
        }
    }

    @Override
    public ResponseEntity<Object> updateNotice(Long noticeId, NoticeReqModel noticeModel) {
        try {
            NoticeEntity noticeEntity = noticeRepository.findById(noticeId)
                    .orElseThrow(() -> new NoticeNotFoundException("Notice not found with ID: " + noticeId));

            //updating the post Entity
            noticeEntity.setTitle(noticeModel.getTitle());
            noticeEntity.setClassroomId(noticeModel.getClassroomId());
            noticeEntity.setTrainerId(noticeModel.getTrainerId());

            MultipartFile file = noticeModel.getFile();
            if (file != null && !file.isEmpty()) {
                String fileUrl = FileService.uploadFile(file,AppConstants.NOTICE_UPLOAD_DIR);
                if (fileUrl == null) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload the file");
                }
                noticeEntity.setFileUrl(fileUrl);
            }
            noticeRepository.save(noticeEntity);
            return ResponseEntity.status(HttpStatus.OK).body("Notice updated successfully");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update Notice");
        }
    }

    @Override
    public ResponseEntity<Object> downloadNoticeFile(Long noticeId) {
        NoticeEntity noticeEntity = noticeRepository.findById(noticeId).orElseThrow(() -> new NoticeNotFoundException("Notice not found with ID: " + noticeId));
        if(noticeEntity.getFileUrl()==null){throw new FileNotFoundException("File not found for download");}

        try{
            File file = new File(noticeEntity.getFileUrl());
            String fileContents = FileUtils.readFileToString(file, StandardCharsets.UTF_8);

            // Create a new file in the specified directory
            String fileName = StringUtils.cleanPath(file.getName());
            File destinationDir = new File(AppConstants.NOTICE_DOWNLOAD_DIR);
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
    public ResponseEntity<Object> getAllNoticeByClassroomId(Long classroomId) {
        List<NoticeEntity> noticeEntityList = noticeRepository.findByClassroomId(classroomId);

        Map<String, Object> response = new HashMap<>();
        response.put("Total Notice", noticeEntityList.size());

        List<Map<String, Object>> notices = new ArrayList<>();
        for (NoticeEntity noticeEntity : noticeEntityList) {
            Map<String, Object> notice = new HashMap<>();
            notice.put("id", noticeEntity.getId());
            notice.put("trainerId", noticeEntity.getTrainerId());
            notice.put("classroomId", noticeEntity.getClassroomId());
            notice.put("title", noticeEntity.getTitle());
            notice.put("createdTime", noticeEntity.getCreatedTime());
            notice.put("fileUrl", noticeEntity.getFileUrl());

            // Fetch the trainer's name using trainerRepository.findById(trainerId)
            Optional<TrainerEntity> trainerOptional = trainerRepository.findById(noticeEntity.getTrainerId());
            if (trainerOptional.isPresent()) {
                TrainerEntity trainerEntity = trainerOptional.get();
                notice.put("trainerName", trainerEntity.getFullName());
            } else {
                notice.put("trainerName", "Trainer not found"); // Or any other default value
            }
            notices.add(notice);
        }

        response.put("Posts", notices);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

package com.backend.tms.service.Impl;

import com.backend.tms.entity.BatchEntity;
import com.backend.tms.entity.NoticeEntity;
import com.backend.tms.entity.PostEntity;
import com.backend.tms.entity.TrainerEntity;
import com.backend.tms.exception.custom.*;
import com.backend.tms.model.Classroom.NoticeReqModel;
import com.backend.tms.model.Classroom.NoticeResModel;
import com.backend.tms.model.Classroom.PostResModel;
import com.backend.tms.repository.BatchRepository;
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

@Service
@RequiredArgsConstructor
public class NoticeServiceImp implements NoticeService {
    private final NoticeRepository noticeRepository;
    private final BatchRepository batchRepository;
    private final TrainerRepository trainerRepository;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<Object> createNotice(NoticeReqModel noticeReqModel) {
        try{
            // Validate if the associated batch exists
            BatchEntity batchEntity = batchRepository.findById(noticeReqModel.getBatchId())
                    .orElseThrow(() -> new BatchNotFoundException("Batch not found"));

            // Validate if the associated trainer exists
            TrainerEntity trainerEntity = trainerRepository.findById(noticeReqModel.getTrainerId())
                    .orElseThrow(() -> new TrainerNotFoundException("Trainer not found"));

            MultipartFile file = noticeReqModel.getFile();
            String fileUrl=null;

            if (!file.isEmpty()) {
                fileUrl = FileService.uploadFile(file,AppConstants.NOTICE_UPLOAD_DIR);
            }
            NoticeEntity noticeEntity = modelMapper.map(noticeReqModel, NoticeEntity.class);
            if (fileUrl != null) {
                noticeEntity.setFileUrl(fileUrl);
            }
            NoticeEntity createdNotice = noticeRepository.save(noticeEntity);
            trainerEntity.getNotices().add(createdNotice);
            return ResponseEntity.status(HttpStatus.CREATED).body("Notice created successfully");
        }catch(Exception e) {
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
            noticeEntity.setNoticeTitle(noticeModel.getNoticeTitle());
            noticeEntity.setBatchId(noticeModel.getBatchId());
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
}

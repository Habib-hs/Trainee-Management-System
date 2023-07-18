package com.backend.tms.service.Impl;

import com.backend.tms.entity.BatchEntity;
import com.backend.tms.entity.NoticeEntity;
import com.backend.tms.entity.PostEntity;
import com.backend.tms.entity.TrainerEntity;
import com.backend.tms.exception.custom.BatchNotFoundException;
import com.backend.tms.exception.custom.TrainerNotFoundException;
import com.backend.tms.model.Classroom.NoticeReqModel;
import com.backend.tms.repository.BatchRepository;
import com.backend.tms.repository.NoticeRepository;
import com.backend.tms.repository.TrainerRepository;
import com.backend.tms.service.NoticeService;
import com.backend.tms.utlis.AppConstants;
import com.backend.tms.utlis.FileService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
        return null;
    }

    @Override
    public ResponseEntity<Object> updateNotice(Long noticeId, NoticeReqModel noticeModel) {
        return null;
    }

    @Override
    public ResponseEntity<Object> downloadNoticeFile(Long noticeId) {
        return null;
    }
}

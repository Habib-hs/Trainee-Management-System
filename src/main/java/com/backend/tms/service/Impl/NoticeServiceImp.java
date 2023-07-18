package com.backend.tms.service.Impl;

import com.backend.tms.model.Classroom.NoticeReqModel;
import com.backend.tms.repository.NoticeRepository;
import com.backend.tms.service.NoticeService;
import com.backend.tms.utlis.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeServiceImp implements NoticeService {
    private final NoticeRepository noticeRepository;

    @Override
    public ResponseEntity<Object> createNotice(NoticeReqModel noticeReqModel) {

    }
}

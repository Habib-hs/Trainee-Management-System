package com.backend.tms.service;

import com.backend.tms.model.Classroom.NoticeReqModel;
import org.springframework.http.ResponseEntity;

public interface NoticeService {
     ResponseEntity<Object> createNotice(NoticeReqModel noticeReqModel);
}

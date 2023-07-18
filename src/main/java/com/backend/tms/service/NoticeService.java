package com.backend.tms.service;

import com.backend.tms.model.Classroom.NoticeReqModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

public interface NoticeService {
     ResponseEntity<Object> createNotice(NoticeReqModel noticeReqModel);
     public ResponseEntity<Object> getNotice(Long noticeId);
     public ResponseEntity<Object> updateNotice(Long noticeId, NoticeReqModel noticeModel);
     public ResponseEntity<Object> downloadNoticeFile(Long noticeId);

}

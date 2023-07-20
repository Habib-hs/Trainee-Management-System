package com.backend.tms.service;

import com.backend.tms.model.Classroom.NoticeNoFileReqModel;
import com.backend.tms.model.Classroom.NoticeReqModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface NoticeService {
     ResponseEntity<Object> createNotice(NoticeReqModel noticeReqModel);
      ResponseEntity<Object> getNotice(Long noticeId);
      ResponseEntity<Object> updateNotice(Long noticeId, NoticeReqModel noticeModel);
      ResponseEntity<Object> downloadNoticeFile(Long noticeId);
     ResponseEntity<Object> getAllNoticeByClassroomId(Long classroomId);


}

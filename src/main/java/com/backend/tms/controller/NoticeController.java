package com.backend.tms.controller;

import com.backend.tms.model.Classroom.NoticeNoFileReqModel;
import com.backend.tms.model.Classroom.NoticeReqModel;
import com.backend.tms.model.Classroom.PostReqModel;
import com.backend.tms.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;
    @PostMapping()
    public ResponseEntity<Object> createNotice(@ModelAttribute NoticeReqModel noticeModel) {
        return noticeService.createNotice(noticeModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getNotice(@PathVariable("id") Long noticeId) {
        return noticeService.getNotice(noticeId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getAllNoticeByClassname(@PathVariable("id") Long noticeId) {
        return noticeService.getNotice(noticeId);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateNotice(@PathVariable("id") Long noticeId, @ModelAttribute NoticeReqModel noticeModel) {
        return noticeService.updateNotice(noticeId, noticeModel);
    }
    @GetMapping("/{id}/download")
    public ResponseEntity<Object> downloadNoticeFile(@PathVariable("id") Long noticeId) {
        return noticeService.downloadNoticeFile(noticeId);
    }

}

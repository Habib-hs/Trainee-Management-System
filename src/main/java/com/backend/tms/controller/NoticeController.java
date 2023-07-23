package com.backend.tms.controller;

import com.backend.tms.model.Classroom.NoticeNoFileReqModel;
import com.backend.tms.model.Classroom.NoticeReqModel;
import com.backend.tms.model.Classroom.PostReqModel;
import com.backend.tms.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;
    @PostMapping()
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<Object> createNotice(@ModelAttribute NoticeReqModel noticeModel) {
        return noticeService.createNotice(noticeModel);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('TRAINER') or hasRole('TRAINEE')")
    public ResponseEntity<Object> getNotice(@PathVariable("id") Long noticeId) {
        return noticeService.getNotice(noticeId);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<Object> updateNotice(@PathVariable("id") Long noticeId, @ModelAttribute NoticeReqModel noticeModel) {
        return noticeService.updateNotice(noticeId, noticeModel);
    }
    @GetMapping("/{id}/download")
    @PreAuthorize("hasRole('TRAINER') or hasRole('TRAINEE')")
    public ResponseEntity<Object> downloadNoticeFile(@PathVariable("id") Long noticeId) {
        return noticeService.downloadNoticeFile(noticeId);
    }

    @GetMapping("/classroom/{id}")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<Object> getAllNoticeByClassroomId(@PathVariable("id") Long classroomId) {
        return noticeService.getAllNoticeByClassroomId(classroomId);
    }


}

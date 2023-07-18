package com.backend.tms.controller;

import com.backend.tms.model.Classroom.NoticeReqModel;
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

}

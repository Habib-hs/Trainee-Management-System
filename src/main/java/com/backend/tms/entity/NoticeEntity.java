package com.backend.tms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notice")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeEntity {
    private Long id;
    private String NoticeTitle;
    private Long BatchId;
    private Long trainerId;
    private String attachment;
}

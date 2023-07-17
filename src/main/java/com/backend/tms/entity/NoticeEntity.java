package com.backend.tms.entity;

import jakarta.persistence.*;
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String NoticeTitle;
    private Long BatchId;
    private Long trainerId;
    private String attachment;
}

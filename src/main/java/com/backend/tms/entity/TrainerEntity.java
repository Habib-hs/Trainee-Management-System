package com.backend.tms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "trainers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String profilePicture;
    private String email;
    private String designation;
    private String joiningDate;
    private int yearsOfExperience;
    private String expertises;
    private String contactNumber;
    private String presentAddress;

    //making relation with user
    @OneToOne(cascade = CascadeType.ALL)
    private UserEntity user;

    //relation with course
    @Builder.Default
    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<CourseEntity> courses = new HashSet<>();

    //relation with batch
    @Builder.Default
    @ManyToMany(mappedBy = "trainers")
    private Set<BatchEntity> batches = new HashSet<>();

    //relation with post
    @Builder.Default
    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<PostEntity> posts = new HashSet<>();

    //relationship with notice
    @Builder.Default
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<NoticeEntity> notices = new HashSet<>();

    //hashcode compare
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainerEntity that = (TrainerEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
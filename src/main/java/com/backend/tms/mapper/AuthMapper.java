package com.backend.tms.mapper;

import com.backend.tms.entity.AdminEntity;
import com.backend.tms.entity.TraineeEntity;
import com.backend.tms.entity.TrainerEntity;
import com.backend.tms.model.Admin.AdminReqModel;
import com.backend.tms.model.Admin.AdminResModel;
import com.backend.tms.model.Trainee.TraineeReqModel;
import com.backend.tms.model.Trainee.TraineeResModel;
import com.backend.tms.model.Trainer.TrainerReqModel;
import com.backend.tms.model.Trainer.TrainerResModel;

public class AuthMapper {
    public static AdminEntity adminReqModeltoEntity(AdminReqModel adminReqModel) {
        return AdminEntity.builder()
                .id(adminReqModel.getId())
                .fullName(adminReqModel.getFullName())
                .gender(adminReqModel.getGender())
                .email(adminReqModel.getEmail())
                .contactNumber(adminReqModel.getContactNumber())
                .build();
    }

    public static TrainerEntity trainerReqModeltoEntity(TrainerReqModel trainerReqModel) {
        return TrainerEntity.builder()
                .id(trainerReqModel.getId())
                .fullName(trainerReqModel.getFullName())
                .email(trainerReqModel.getEmail())
                .profilePicture(trainerReqModel.getProfilePicture())
                .designation(trainerReqModel.getDesignation())
                .joiningDate(trainerReqModel.getJoiningDate())
                .yearsOfExperience(trainerReqModel.getYearsOfExperience())
                .expertises(trainerReqModel.getExpertises())
                .contactNumber(trainerReqModel.getContactNumber())
                .presentAddress(trainerReqModel.getPresentAddress())
                .build();
    }

    public static TraineeEntity traineeReqModeltoEntity(TraineeReqModel traineeReqModel) {
        return TraineeEntity.builder()
                .id(traineeReqModel.getId())
                .fullName(traineeReqModel.getFullName())
                .profilePicture(traineeReqModel.getProfilePicture())
                .gender(traineeReqModel.getGender())
                .dateOfBirth(traineeReqModel.getDateOfBirth())
                .email(traineeReqModel.getEmail())
                .password(traineeReqModel.getPassword())
                .contactNumber(traineeReqModel.getContactNumber())
                .degreeName(traineeReqModel.getDegreeName())
                .educationalInstitute(traineeReqModel.getEducationalInstitute())
                .domain(traineeReqModel.getDomain())
                .cgpa(traineeReqModel.getCgpa())
                .passingYear(traineeReqModel.getPassingYear())
                .presentAddress(traineeReqModel.getPresentAddress())
                .build();
    }

    public static AdminResModel adminEntityResModel(AdminEntity adminEntity) {
        return AdminResModel.builder()
                .message("Admin created successfully")
                .build();
    }

    public static TraineeResModel traineeEntityResModel(TraineeEntity traineeEntity) {
        return TraineeResModel.builder()
                .message("Trainee added successfully")
                .build();
    }

    public static TrainerResModel trainerEntityResModel(TrainerEntity trainerEntity) {
        return TrainerResModel.builder()
                .message("Trainer added successfully")
                .build();
    }

    
}

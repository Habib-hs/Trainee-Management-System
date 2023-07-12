package com.backend.tms.service.Impl;

import com.backend.tms.entity.AdminEntity;
import com.backend.tms.entity.TraineeEntity;
import com.backend.tms.entity.TrainerEntity;
import com.backend.tms.entity.UserEntity;
import com.backend.tms.exception.custom.TraineeAlreadyExistsException;
import com.backend.tms.mapper.AuthMapper;
import com.backend.tms.model.Admin.AdminReqModel;
import com.backend.tms.model.Admin.AdminResModel;
import com.backend.tms.model.Common.AuthenticationReqModel;
import com.backend.tms.model.Common.AuthenticationResModel;
import com.backend.tms.model.Trainee.TraineeReqModel;
import com.backend.tms.model.Trainee.TraineeResModel;
import com.backend.tms.model.Trainer.TrainerReqModel;
import com.backend.tms.model.Trainer.TrainerResModel;
import com.backend.tms.repository.AdminRepository;
import com.backend.tms.repository.TraineeRepository;
import com.backend.tms.repository.TrainerRepository;
import com.backend.tms.repository.UserRepository;
import com.backend.tms.service.AuthService;
import com.backend.tms.utlis.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AdminRepository adminRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;

    @Override
    public ResponseEntity<Object> registerAdmin(AdminReqModel adminModel) {

        UserEntity userEntityByEmail = userRepository.findByEmail(adminModel.getEmail());
        if (userEntityByEmail != null) {
            System.out.println("Admin already exists with the given email");
        }

        // Create a new UserEntity
        UserEntity userEntity = UserEntity.builder()
                .id(adminModel.getId()) // Set the ID explicitly
                .email(adminModel.getEmail())
                .password(passwordEncoder.encode(adminModel.getPassword()))
                .role("Admin")
                .build();

        // Save the UserEntity to generate the ID
        UserEntity newUser = userRepository.save(userEntity);

        //mapping the admin
        AdminEntity admin = AuthMapper.adminReqModeltoEntity(adminModel);
        admin.setUser(newUser); // Set the UserEntity as the associated user

        // Save the AdminEntity
        adminRepository.save(admin);

        // Map the saved AdminEntity to AdminResModel
        AdminResModel adminResModel = AuthMapper.adminEntityResModel(admin);

        // Return the AdminResModel as the response
        return new ResponseEntity<>(adminResModel, HttpStatus.OK);


    }

    @Override
    public ResponseEntity<Object> registerTrainee(TraineeReqModel traineeModel) {

        UserEntity userEntityByEmail = userRepository.findByEmail(traineeModel.getEmail());
        if (userEntityByEmail != null) {
            throw new TraineeAlreadyExistsException("User already exists with the given email");
        }

        // Create a new UserEntity
        UserEntity userTrainee = UserEntity.builder()
                .id(traineeModel.getId()) // Set the ID explicitly
                .email(traineeModel.getEmail())
                .password(passwordEncoder.encode(traineeModel.getPassword()))
                .role("Trainee")
                .build();

        // Save the UserEntity to generate the ID
        UserEntity newUser = userRepository.save(userTrainee);


        // Map TraineeReqModel to TraineeEntity
        TraineeEntity trainee = AuthMapper.traineeReqModeltoEntity(traineeModel);
        trainee.setUser(newUser); // Set the UserEntity as the associated user

        // Save the TraineeEntity
        traineeRepository.save(trainee);

        // Map the saved TraineeEntity to TraineeResModel
        TraineeResModel traineeResModel = AuthMapper.traineeEntityResModel(trainee);

        // Return the TraineeResModel as the response
        return new ResponseEntity<>(traineeResModel, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> registerTrainer(TrainerReqModel trainerModel) {

        // Check if the trainer with the given email already exists
        if (userRepository.findByEmail(trainerModel.getEmail()) != null) {
            throw new TraineeAlreadyExistsException("User already exists with the given email");
        }

        // Create a new UserEntity
        UserEntity userEntity = UserEntity.builder()
                .id(trainerModel.getId()) // Set the ID explicitly
                .email(trainerModel.getEmail())
                .password(passwordEncoder.encode(trainerModel.getPassword()))
                .role("Trainer")
                .build();

        // Save the UserEntity to generate the ID
        UserEntity newUser = userRepository.save(userEntity);

        // Map TrainerReqModel to TrainerEntity
        TrainerEntity trainer = AuthMapper.trainerReqModeltoEntity(trainerModel);
        trainer.setUser(newUser);
        // Save the TrainerEntity
        trainerRepository.save(trainer);
        TrainerResModel trainerResModel = AuthMapper.trainerEntityResModel(trainer);

        // Return the TrainerResModel as the response
        return new ResponseEntity<>(trainerResModel, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Object> login(AuthenticationReqModel requestModel) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestModel.getEmail(),
                        requestModel.getPassword()
                )
        );
        var user = userRepository.findByEmail(requestModel.getEmail());
        var jwtToken = jwtService.generateToken(user);
        AuthenticationResModel authenticationResponse = AuthenticationResModel.builder()
                .message("Successfully Login!")
                .token(jwtToken)
                .build();
        return ResponseEntity.ok(authenticationResponse);
    }
}

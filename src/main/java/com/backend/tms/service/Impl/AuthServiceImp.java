package com.backend.tms.service.Impl;

import com.backend.tms.entity.AdminEntity;
import com.backend.tms.entity.TraineeEntity;
import com.backend.tms.entity.TrainerEntity;
import com.backend.tms.entity.UserEntity;
import com.backend.tms.exception.custom.AdminAlreadyExistException;
import com.backend.tms.exception.custom.TraineeAlreadyExistsException;
import com.backend.tms.exception.custom.TrainerAlreadyExistException;
import com.backend.tms.model.Admin.AdminReqModel;
import com.backend.tms.model.Admin.AdminResModel;
import com.backend.tms.model.Common.AuthenticationReqModel;
import com.backend.tms.model.Common.AuthenticationResModel;
import com.backend.tms.model.Trainee.TraineeReqModel;
import com.backend.tms.model.Trainer.TrainerReqModel;
import com.backend.tms.repository.AdminRepository;
import com.backend.tms.repository.TraineeRepository;
import com.backend.tms.repository.TrainerRepository;
import com.backend.tms.repository.UserRepository;
import com.backend.tms.service.AuthService;
import com.backend.tms.utlis.JwtService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<Object> registerAdmin(AdminReqModel adminModel) {

        UserEntity userEntityByEmail = userRepository.findByEmail(adminModel.getEmail());
        if (userEntityByEmail != null) {
            throw new AdminAlreadyExistException("User already exists with the given email");
        }

        // Create a new UserEntity
        UserEntity userEntity = UserEntity.builder()
                .id(adminModel.getId()) // Set the ID explicitly
                .email(adminModel.getEmail())
                .password(passwordEncoder.encode(adminModel.getPassword()))
                .role("ADMIN")
                .build();

        // Save the UserEntity to generate the ID
        UserEntity newUser = userRepository.save(userEntity);

        //mapping the admin
        AdminEntity admin = modelMapper.map(adminModel, AdminEntity.class);
        admin.setUser(newUser); // Set the UserEntity as the associated user
        // Save the AdminEntity
        adminRepository.save(admin);
        return new ResponseEntity<>("admin created successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> registerTrainee(TraineeReqModel traineeModel) {

        UserEntity userEntityByEmail = userRepository.findByEmail(traineeModel.getEmail());
        if (userEntityByEmail != null) {
            throw new TraineeAlreadyExistsException("User already exists with the given email");
        }
        // Create a new UserEntity
        UserEntity userTrainee = UserEntity.builder()
                .id(traineeModel.getId())
                .email(traineeModel.getEmail())
                .password(passwordEncoder.encode(traineeModel.getPassword()))
                .role("TRAINEE")
                .build();

        // Save the UserEntity to generate the ID
        UserEntity newUser = userRepository.save(userTrainee);

        // Map TraineeReqModel to TraineeEntity
        TraineeEntity trainee = modelMapper.map(traineeModel, TraineeEntity.class);
        trainee.setUser(newUser);
        traineeRepository.save(trainee);
        //add trainee Id on the User table
        newUser.setRoleBasedId(trainee.getId());
        userRepository.save(newUser);
        return new ResponseEntity<>("Trainee Created Successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> registerTrainer(TrainerReqModel trainerModel) {

        // Check if the trainer with the given email already exists
        if (userRepository.findByEmail(trainerModel.getEmail()) != null) {
            throw new TrainerAlreadyExistException("User already exists with the given email");
        }

        // Create a new UserEntity
        UserEntity userEntity = UserEntity.builder()
                .id(trainerModel.getId()) // Set the ID explicitly
                .email(trainerModel.getEmail())
                .password(passwordEncoder.encode(trainerModel.getPassword()))
                .role("TRAINER")
                .build();

        // Save the UserEntity to generate the ID
        UserEntity newUser = userRepository.save(userEntity);

        // Map TrainerReqModel to TrainerEntity
        TrainerEntity trainer = modelMapper.map(trainerModel, TrainerEntity.class);
        trainer.setUser(newUser);
        trainerRepository.save(trainer);
        //add the trainerId
        newUser.setRoleBasedId(trainer.getId());
        userRepository.save(newUser);
        return new ResponseEntity<>("trainer created successfully", HttpStatus.OK);

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
                .roleBasedId(user.getRoleBasedId())
                .token(jwtToken)
                .build();
        return ResponseEntity.ok(authenticationResponse);
    }
}

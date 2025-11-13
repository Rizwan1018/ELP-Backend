package com.elearning.backend.service;

import com.elearning.backend.dto.AuthDTO.*;
import com.elearning.backend.entity.*;
import com.elearning.backend.exception.EmailAlreadyExistsException;
import com.elearning.backend.exception.InvalidPasswordException;
import com.elearning.backend.exception.UserNotFoundException;
import com.elearning.backend.repository.*;
import com.elearning.backend.security.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.InvalidAlgorithmParameterException;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class AuthService {

    private final UserRepository userRepo;
    private final StudentRepository studentRepo;
    private final InstructorRepository instructorRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepo, StudentRepository studentRepo,
                       InstructorRepository instructorRepo, PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepo = userRepo;
        this.studentRepo = studentRepo;
        this.instructorRepo = instructorRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    public AuthResponse signup(SignupRequest request) {
        if (userRepo.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        log.info("Full name"+ request.fullName());

        User user = new User();
        user.setFullName(request.fullName());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(request.role());
        userRepo.saveAndFlush(user);

        if (request.role() == Role.STUDENT) {
            Student s = new Student();
            s.setId(user.getId());
            s.setName(user.getFullName());
            s.setEmail(user.getEmail());
            studentRepo.save(s);
        } else {
            Instructor ins = new Instructor();
            ins.setId(user.getId());
            ins.setName(user.getFullName());
            ins.setEmail(user.getEmail());
            instructorRepo.save(ins);
        }

        String token = jwtService.generateToken(user.getEmail(), Map.of("role", user.getRole().name()));

        return new AuthResponse(user.getId(), user.getFullName(), user.getEmail(), user.getRole(), token);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepo.findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidPasswordException("Invalid password");
        }

        String token = jwtService.generateToken(user.getEmail(), Map.of("role", user.getRole().name()));

        return new AuthResponse(user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole(),
                token);
    }
    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email.trim().toLowerCase());
    }

    public void updatePassword(String email, String newPassword) {
        Optional<User> userOpt = userRepo.findByEmail(email.trim().toLowerCase());
        userOpt.ifPresent(user -> {
            user.setPassword(passwordEncoder.encode(newPassword)); // Hash the password
            userRepo.save(user);
        });
    }

}

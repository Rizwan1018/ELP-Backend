package com.elearning.backend.service;

import com.elearning.backend.dto.AuthDTO.*;
import com.elearning.backend.model.*;
import com.elearning.backend.repository.*;
import com.elearning.backend.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
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
            throw new RuntimeException("Email already exists");
        }
        System.out.println("Full name"+ request.fullName());

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
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(user.getEmail(), Map.of("role", user.getRole().name()));

        return new AuthResponse(user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole(),
                token);
    }
}

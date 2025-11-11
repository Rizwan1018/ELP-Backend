package com.elearning.backend.controller;

import com.elearning.backend.dto.AuthDTO;
import com.elearning.backend.entity.User;
import com.elearning.backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth")
@Slf4j
@Tag(name = "Authentication APIs", description = " ")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }


    @Operation(
            summary = "Register a new user",
            description = "Creates a student or instructor based on role"
    )
    @PostMapping("/signup")
    public AuthDTO.AuthResponse signup(@RequestBody AuthDTO.SignupRequest request){
        log.info(""+request);
        return authService.signup(request);
    }

    @PostMapping("/login")
    public AuthDTO.AuthResponse login(@RequestBody AuthDTO.LoginRequest request){

        return authService.login(request);
    }
    @GetMapping("/find-account/{email}")
    public ResponseEntity<?> findAccount(@PathVariable String email) {
        log.info("Received email for account search: {}", email);
        Optional<User> user = authService.findByEmail(email);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        }
    }

    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String newPassword = payload.get("newPassword");
        String confirmPassword = payload.get("confirmPassword");

        if (!newPassword.equals(confirmPassword)) {
            return ResponseEntity.badRequest().body("Passwords do not match");
        }

        Optional<User> userOpt = authService.findByEmail(email);
        if (userOpt.isPresent()) {
            authService.updatePassword(email, newPassword);
            return ResponseEntity.ok(userOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }


}

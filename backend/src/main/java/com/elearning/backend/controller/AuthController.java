package com.elearning.backend.controller;

import com.elearning.backend.dto.AuthDTO;
import com.elearning.backend.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/signup")
    public AuthDTO.AuthResponse signup(@RequestBody AuthDTO.SignupRequest request){
        log.info(""+request);
        return authService.signup(request);
    }

    @PostMapping("/login")
    public AuthDTO.AuthResponse login(@RequestBody AuthDTO.LoginRequest request){
        return authService.login(request);
    }


}

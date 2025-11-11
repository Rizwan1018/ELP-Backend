package com.elearning.backend.dto;

import com.elearning.backend.entity.Role;

public class AuthDTO {

    public record SignupRequest(String fullName, String email, String password, Role role) {}

    public record LoginRequest(String email, String password){}

    public record AuthResponse( Long id,String fullName, String email, Role role, String token){}
}

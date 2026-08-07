package com.microservices.stylecartbackend.auth.service;

import com.microservices.stylecartbackend.dto.AuthResponse;
import com.microservices.stylecartbackend.dto.LoginRequest;
import com.microservices.stylecartbackend.dto.RefreshTokenRequest;
import com.microservices.stylecartbackend.dto.RegisterRequest;
import com.microservices.stylecartbackend.dto.UserResponse;

public interface AuthService {

    UserResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refreshToken(RefreshTokenRequest request);
}
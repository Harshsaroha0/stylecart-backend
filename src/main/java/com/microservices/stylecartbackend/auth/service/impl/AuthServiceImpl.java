package com.microservices.stylecartbackend.auth.service.impl;

import com.microservices.stylecartbackend.auth.service.AuthService;
import com.microservices.stylecartbackend.auth.service.security.CustomUserDetailsService;
import com.microservices.stylecartbackend.auth.service.security.JwtService;
import com.microservices.stylecartbackend.auth.service.security.UserPrincipal;
import com.microservices.stylecartbackend.constant.RoleConstants;
import com.microservices.stylecartbackend.dto.AuthResponse;
import com.microservices.stylecartbackend.dto.LoginRequest;
import com.microservices.stylecartbackend.dto.RefreshTokenRequest;
import com.microservices.stylecartbackend.dto.RegisterRequest;
import com.microservices.stylecartbackend.dto.UserResponse;
import com.microservices.stylecartbackend.entity.Role;
import com.microservices.stylecartbackend.entity.User;
import com.microservices.stylecartbackend.exception.EmailAlreadyExistsException;
import com.microservices.stylecartbackend.exception.ResourceNotFoundException;
import com.microservices.stylecartbackend.repository.RoleRepository;
import com.microservices.stylecartbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    public UserResponse register(RegisterRequest request) {

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        // Fetch default customer role
        Role customerRole = roleRepository
                .findByName(RoleConstants.ROLE_CUSTOMER)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role",
                                "name",
                                RoleConstants.ROLE_CUSTOMER
                        ));

        // Create user
        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(customerRole);

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail(),
                savedUser.getRole().getName()
        );
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserPrincipal userPrincipal =
                (UserPrincipal) userDetailsService.loadUserByUsername(
                        request.getEmail()
                );

        String accessToken =
                jwtService.generateAccessToken(userPrincipal);

        String refreshToken =
                jwtService.generateRefreshToken(userPrincipal);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .build();
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {

        String refreshToken = request.getRefreshToken();

        String email = jwtService.extractUsername(refreshToken);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        UserPrincipal principal = new UserPrincipal(user);

        if (!jwtService.isTokenValid(refreshToken, principal)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String newAccessToken =
                jwtService.generateAccessToken(principal);

        String newRefreshToken =
                jwtService.generateRefreshToken(principal);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .build();
    }
}
package com.tim.foodwastetracker.service;

import com.tim.foodwastetracker.dto.AuthenticationRequest;
import com.tim.foodwastetracker.dto.AuthenticationResponse;
import com.tim.foodwastetracker.dto.RegisterRequest;
import com.tim.foodwastetracker.exception.AuthenticationException;
import com.tim.foodwastetracker.exception.UserNotActiveException;
import com.tim.foodwastetracker.exception.UserNotFoundException;
import com.tim.foodwastetracker.jwt.JwtService;
import com.tim.foodwastetracker.model.*;
import com.tim.foodwastetracker.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        // Validate password
        validatePassword(request.password());

        var user = User.builder()
                .email(request.email())
                .userRole(UserRole.USER)
                .isActive(true) // Set to false until email is verified
                .createdAt(LocalDate.now())
                .firstname(request.firstname())
                .lastname(request.lastname())
                .password(passwordEncoder.encode(request.password()))
                .build();
        userRepository.save(user);

        log.info("Registered new user with email: " + request.email());

        var jwt = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwt)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        String username = request.email();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found for email: " + username));
        if (!user.isEnabled()) {
            throw new UserNotActiveException("User account is not active. Please check your email for the activation link.");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            request.password()
                    )
            );
            // Authentication was successful
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtService.generateToken(user);

            log.info("User with email " + username + " authenticated successfully.");

            return AuthenticationResponse.builder()
                    .email(username)
                    .token(jwt)
                    .build();
        } catch (org.springframework.security.core.AuthenticationException ex) {
            if (ex instanceof LockedException) {
                throw new AuthenticationException("User account is locked. Please contact support.");
            } else if (ex instanceof BadCredentialsException) {
                throw new AuthenticationException("Invalid username or password.");
            } else {
                throw new AuthenticationException("Authentication failed: " + ex.getMessage());
            }
        }
    }

    private void validatePassword(String password) {
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long.");
        }
        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password must contain at least one digit.");
        }
        if (!password.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("Password must contain at least one lowercase letter.");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Password must contain at least one uppercase letter.");
        }
        if (!password.matches(".*[!@#$%^&*()].*")) {
            throw new IllegalArgumentException("Password must contain at least one special character (!@#$%^&*()).");
        }
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return userRepository.findByEmail(currentPrincipalName)
                .orElseThrow(() -> new UserNotFoundException("User name not found - " + currentPrincipalName));
    }
}

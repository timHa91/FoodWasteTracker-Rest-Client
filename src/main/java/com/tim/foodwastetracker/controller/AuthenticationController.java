package com.tim.foodwastetracker.controller;

import com.tim.foodwastetracker.dto.AuthenticationRequest;
import com.tim.foodwastetracker.dto.AuthenticationResponse;
import com.tim.foodwastetracker.dto.RegisterRequest;
import com.tim.foodwastetracker.service.AuthenticationService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@Log4j2
public class AuthenticationController {

    private final AuthenticationService authService;

    @ApiOperation(value = "Register a new User")
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterRequest request) {
        log.info("Received request to register user: {}", request.email());
        return ResponseEntity.ok(authService.register(request));
    }

    @ApiOperation(value = "Login with a User")
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest request) {
        log.info("Received request to authenticate user: {}", request.email());
        return ResponseEntity.ok(authService.authenticate(request));
    }
}

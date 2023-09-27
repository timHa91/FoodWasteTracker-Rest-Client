package com.tim.foodwastetracker.service;

import com.tim.foodwastetracker.config.SecurityConfig;
import com.tim.foodwastetracker.dto.AuthenticationRequest;
import com.tim.foodwastetracker.dto.AuthenticationResponse;
import com.tim.foodwastetracker.dto.RegisterRequest;
import com.tim.foodwastetracker.exception.UserNotActiveException;
import com.tim.foodwastetracker.exception.UserNotFoundException;
import com.tim.foodwastetracker.jwt.JwtService;
import com.tim.foodwastetracker.model.User;
import com.tim.foodwastetracker.model.UserRole;
import com.tim.foodwastetracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService underTest;
    @Mock
    private  UserRepository userRepository;
    @Mock
    private  PasswordEncoder passwordEncoder;
    @Mock
    private  JwtService jwtService;
    @Mock
    private  AuthenticationManager authenticationManager;
    @Mock
    private SecurityConfig securityConfig;

    @Test
    void canRegisterNewUser() {
        RegisterRequest request = new RegisterRequest(
                "testFirst",
                "testLast",
                "test@test.com",
                "thnbefT!mb3.1");

        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(request.password())).thenReturn(encodedPassword);

        underTest.register(request);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();

        assertThat(capturedUser.getFirstname()).isEqualTo(request.firstname());
        assertThat(capturedUser.getLastname()).isEqualTo(request.lastname());
        assertThat(capturedUser.getEmail()).isEqualTo(request.email());
        assertThat(capturedUser.getPassword()).isEqualTo(encodedPassword);
    }

    @Test
    void canUserAuthenticate() {
        String email = "test@test.com";
        String password = "thnbefT!mb3.1";
        User user = new User(
                1L,
                "testPassword",
                email,
                "TestFirst",
                "TestLast",
                LocalDate.now(),
                LocalDate.now(),
                true,
                UserRole.USER);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any())).thenReturn(mock(Authentication.class));
        when(jwtService.generateToken(user)).thenReturn("token");

        AuthenticationResponse response = underTest.authenticate(new AuthenticationRequest(email, password));

        assertThat(response.email()).isEqualTo(email);
        assertThat(response.token()).isEqualTo("token");
    }

    @Test
    void canGetCurrentUser() {
        String email = "test@test.com";
        User user = new User(
                1L,
                "testPassword",
                email,
                "TestFirst",
                "TestLast",
                LocalDate.now(),
                LocalDate.now(),
                true,
                UserRole.USER);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(email);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        User result = underTest.getCurrentUser();

        assertEquals(user, result);
    }

    @Test
    void getCurrentUserThrowsWhenUserNotFound() {
        String email = "test@test.com";

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(email);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        assertThrows(UserNotFoundException.class, () -> underTest.getCurrentUser());
    }

    @Test
    void registerThrowsWhenPasswordIsInvalid() {
        RegisterRequest request = new RegisterRequest(
                "testFirst",
                "testLast",
                "test@test.com",
                "invalidPassword");

        assertThrows(IllegalArgumentException.class, () -> underTest.register(request));
    }

    @Test
    void authenticateThrowsWhenUserNotFound() {
        String email = "test@test.com";
        String password = "thnbefT!mb3.1";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> underTest.authenticate(new AuthenticationRequest(email, password)));
    }

    @Test
    void authenticateThrowsWhenUserNotActive() {
        String email = "test@test.com";
        String password = "thnbefT!mb3.1";
        User user = new User(
                1L,
                "testPassword",
                email,
                "TestFirst",
                "TestLast",
                LocalDate.now(),
                LocalDate.now(),
                false,
                UserRole.USER);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        assertThrows(UserNotActiveException.class, () -> underTest.authenticate(new AuthenticationRequest(email, password)));
    }
}
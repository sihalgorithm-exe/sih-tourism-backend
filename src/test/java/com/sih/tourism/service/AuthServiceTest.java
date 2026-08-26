package com.sih.tourism.service;

import com.sih.tourism.dto.request.LoginRequest;
import com.sih.tourism.dto.request.RegisterRequest;
import com.sih.tourism.dto.response.AuthResponse;
import com.sih.tourism.entity.Role;
import com.sih.tourism.entity.User;
import com.sih.tourism.exception.DuplicateResourceException;
import com.sih.tourism.exception.InvalidCredentialsException;
import com.sih.tourism.repository.UserRepository;
import com.sih.tourism.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setName("Test User");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("plainPassword123");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("plainPassword123");
    }

    @Test
    void registerThrowsWhenEmailAlreadyExists() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> authService.register(registerRequest));
        verify(userRepository, never()).save(any());
    }

    @Test
    void registerHashesPasswordAndNeverStoresPlaintext() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("plainPassword123")).thenReturn("hashedValue");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            ReflectionTestUtils.setField(u, "id", 1L);
            return u;
        });
        when(jwtUtil.generateToken(1L, "test@example.com", "TOURIST")).thenReturn("fake-jwt-token");

        AuthResponse response = authService.register(registerRequest);

        assertEquals("fake-jwt-token", response.getToken());
        assertEquals(Role.TOURIST.name(), response.getRole());

        verify(userRepository).save(argThat(u ->
                u.getPasswordHash().equals("hashedValue") &&
                !u.getPasswordHash().equals("plainPassword123")
        ));
    }

    @Test
    void loginThrowsWhenUserNotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> authService.login(loginRequest));
    }

    @Test
    void loginThrowsWhenPasswordDoesNotMatch() {
        User existingUser = new User("Test User", "test@example.com", "hashedValue", Role.TOURIST);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("plainPassword123", "hashedValue")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> authService.login(loginRequest));
    }

    @Test
    void loginSucceedsAndReturnsToken() {
        User existingUser = new User("Test User", "test@example.com", "hashedValue", Role.TOURIST);
        ReflectionTestUtils.setField(existingUser, "id", 1L);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("plainPassword123", "hashedValue")).thenReturn(true);
        when(jwtUtil.generateToken(1L, "test@example.com", "TOURIST")).thenReturn("fake-jwt-token");

        AuthResponse response = authService.login(loginRequest);

        assertEquals("fake-jwt-token", response.getToken());
        assertEquals("test@example.com", response.getEmail());
    }
}

package com.microservicio_usuarios.api.infrastructure.controller;

import com.microservicio_usuarios.api.application.service.UserService;
import com.microservicio_usuarios.api.domain.model.User;
import com.microservicio_usuarios.api.domain.model.UserDTO;
import com.microservicio_usuarios.api.domain.model.UserResponseDTO;
import com.microservicio_usuarios.api.domain.repository.UserRepository;
import com.microservicio_usuarios.api.infrastructure.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TestAuthController {

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserController userController;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setUsername("Usuario test de pruebas uno");
        user.setEmail("test@example.com");
    }

    @Test
    void testRegisterUser() {
        when(userService.register(user)).thenReturn(Mono.just(user));
        when(jwtService.generateToken(user.getUsername())).thenReturn("mockedToken");

        Mono<ResponseEntity<UserResponseDTO>> responseMono = userController.register(user);

        StepVerifier.create(responseMono)
                .assertNext(response -> {
                    assertEquals(200, response.getStatusCodeValue());
                    assertEquals("test@example.com", response.getBody().getEmail());
                })
                .verifyComplete();
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Mono.just(user));

        Mono<ResponseEntity<UserDTO>> responseMono = userController.getUserById(1L);

        StepVerifier.create(responseMono)
                .assertNext(response -> {
                    assertEquals(200, response.getStatusCodeValue());
                    assertEquals("Usuario test de pruebas uno", response.getBody().getName());
                })
                .verifyComplete();
    }

    @Test
    void testDeleteUser() {
        when(userRepository.findById(1L)).thenReturn(Mono.just(user));
        when(userRepository.delete(user)).thenReturn(Mono.empty());

        Mono<ResponseEntity<Object>> responseMono = userController.deleteUser(1L);

        StepVerifier.create(responseMono)
                .assertNext(response -> assertEquals(204, response.getStatusCodeValue()))
                .verifyComplete();
    }

    @Test
    void testUpdateUser() {
        User updatedUser = new User();
        updatedUser.setUsername("Usuario test de pruebas");
        updatedUser.setEmail("nuevousuariotest@gmail.com");

        when(userRepository.findById(1L)).thenReturn(Mono.just(user));
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(updatedUser));

        Mono<ResponseEntity<User>> responseMono = userController.updateUser(1L, updatedUser);

        StepVerifier.create(responseMono)
                .assertNext(response -> assertEquals("Usuario test de pruebas", Objects.requireNonNull(response.getBody()).getUsername()))
                .verifyComplete();
    }
}

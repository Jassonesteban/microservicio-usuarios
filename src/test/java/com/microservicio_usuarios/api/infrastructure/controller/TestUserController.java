package com.microservicio_usuarios.api.infrastructure.controller;

import com.microservicio_usuarios.api.application.service.UserService;
import com.microservicio_usuarios.api.domain.model.User;
import com.microservicio_usuarios.api.domain.model.UserDTO;
import com.microservicio_usuarios.api.domain.model.UserResponseDTO;
import com.microservicio_usuarios.api.domain.repository.UserRepository;
import com.microservicio_usuarios.api.infrastructure.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@ExtendWith(MockitoExtension.class)
public class TestUserController {

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

    private WebTestClient webTestClient;

    private User mockUser;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(userController).build();

        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testUser");
        mockUser.setEmail("test@example.com");
        mockUser.setCardIds(List.of("card1", "card2"));
    }

    @Test
    void testRegisterUser() {
        when(userService.register(any(User.class))).thenReturn(Mono.just(mockUser));
        when(JwtService.generateToken(mockUser.getUsername())).thenReturn("mockToken");

        webTestClient.post()
                .uri("/api/v1/usuarios/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(mockUser))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseDTO.class)
                .value(response -> {
                    assert response.getUsername().equals(mockUser.getUsername());
                    assert response.getEmail().equals(mockUser.getEmail());
                    assert response.getToken().equals("mockToken");
                });

        verify(userService, times(1)).register(any(User.class));
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Mono.just(mockUser));

        webTestClient.get()
                .uri("/api/v1/usuarios/me/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDTO.class)
                .value(userDTO -> {
                    assert userDTO.getId().equals(mockUser.getId());
                    assert userDTO.getName().equals(mockUser.getUsername());
                    assert userDTO.getEmail().equals(mockUser.getEmail());
                });

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/api/v1/usuarios/me/1")
                .exchange()
                .expectStatus().isNotFound();

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteUser() {
        when(userRepository.findById(1L)).thenReturn(Mono.just(mockUser));
        when(userRepository.delete(mockUser)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/v1/usuarios/1")
                .exchange()
                .expectStatus().isNoContent();

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).delete(mockUser);
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/v1/usuarios/1")
                .exchange()
                .expectStatus().isNotFound();

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateUser() {
        User updatedUser = new User();
        updatedUser.setUsername("updatedUser");
        updatedUser.setEmail("updated@example.com");

        when(userRepository.findById(1L)).thenReturn(Mono.just(mockUser));
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(updatedUser));

        webTestClient.put()
                .uri("/api/v1/usuarios/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(updatedUser))
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .value(response -> {
                    assert response.getUsername().equals("updatedUser");
                    assert response.getEmail().equals("updated@example.com");
                });

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }
}

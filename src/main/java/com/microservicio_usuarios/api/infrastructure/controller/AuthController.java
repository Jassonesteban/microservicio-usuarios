package com.microservicio_usuarios.api.infrastructure.controller;

import com.microservicio_usuarios.api.application.service.AuthService;
import com.microservicio_usuarios.api.domain.model.AuthRequest;
import com.microservicio_usuarios.api.domain.model.AuthResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/usuarios")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Mono<AuthResponse> login(@RequestBody AuthRequest request) {
        return authService.authenticate(request);
    }
}

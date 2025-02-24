package com.microservicio_usuarios.api.application.service;

import com.microservicio_usuarios.api.domain.model.AuthRequest;
import com.microservicio_usuarios.api.domain.model.AuthResponse;
import com.microservicio_usuarios.api.domain.repository.UserRepository;
import com.microservicio_usuarios.api.infrastructure.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Mono<AuthResponse> authenticate(AuthRequest request) {
        return userRepository.findByEmail(request.getEmail())
                .flatMap(user -> {
                    if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                        String token = jwtUtil.generateToken(user.getUsername());
                        return Mono.just(new AuthResponse(token, user.getEmail(), user.getUsername(), user.getId()));
                    }
                    return Mono.error(new RuntimeException("Credenciales inválidas"));
                });
    }
}

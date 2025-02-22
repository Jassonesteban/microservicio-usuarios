package com.microservicio_usuarios.api.application.service;

import com.microservicio_usuarios.api.domain.model.User;
import com.microservicio_usuarios.api.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder  passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Mono<User> register(User user) {
        user = new User(user.getId(), user.getUsername(), user.getEmail(), passwordEncoder.encode(user.getPassword()), user.getCardIds());
        return userRepository.save(user);
    }
}

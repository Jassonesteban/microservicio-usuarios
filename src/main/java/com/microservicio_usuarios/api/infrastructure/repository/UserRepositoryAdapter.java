package com.microservicio_usuarios.api.infrastructure.repository;

import com.microservicio_usuarios.api.domain.model.User;
import com.microservicio_usuarios.api.domain.repository.UserRepository;
import com.microservicio_usuarios.api.infrastructure.entity.UserEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UserRepositoryAdapter{

    private final UserRepository userRepository;

    public UserRepositoryAdapter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<User> save(User user) {
        return userRepository.save(user);
    }

    public Mono<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Mono<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Mono<Void> deleteById(Long id) {
        return userRepository.deleteById(id);
    }
}

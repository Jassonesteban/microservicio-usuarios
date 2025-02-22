package com.microservicio_usuarios.api.domain.repository;

import com.microservicio_usuarios.api.domain.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {
    Mono<User> findByEmail(String email);
    Mono<User> findByUsername(String username);
    Mono<Boolean> existsByUsername(String username);
    Mono<Boolean> existsByEmail(String email);
    Mono<Void> deleteByUsername(String username);
    Mono<User> findById(Long id);
}

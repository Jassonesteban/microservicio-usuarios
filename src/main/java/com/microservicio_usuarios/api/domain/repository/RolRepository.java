package com.microservicio_usuarios.api.domain.repository;

import com.microservicio_usuarios.api.domain.model.Rol;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface RolRepository extends ReactiveCrudRepository<Rol, Long> {
    Mono<Rol> findByName(String name);
}

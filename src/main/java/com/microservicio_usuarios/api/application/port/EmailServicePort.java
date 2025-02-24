package com.microservicio_usuarios.api.application.port;

import com.microservicio_usuarios.api.domain.model.Email;
import reactor.core.publisher.Mono;

public interface EmailServicePort {
    Mono<Void> sendEmail(Email email);
}

package com.microservicio_usuarios.api.application.usecase;

import com.microservicio_usuarios.api.application.port.EmailServicePort;
import com.microservicio_usuarios.api.domain.model.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SendEmailUseCase {

    private final EmailServicePort emailServicePort;

    public Mono<Void> execute(Email email) {
        return emailServicePort.sendEmail(email);
    }
}

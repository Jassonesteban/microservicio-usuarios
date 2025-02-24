package com.microservicio_usuarios.api.infrastructure.controller;

import com.microservicio_usuarios.api.application.usecase.SendEmailUseCase;
import com.microservicio_usuarios.api.domain.model.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final SendEmailUseCase sendEmailUseCase;

    public EmailController(SendEmailUseCase sendEmailUseCase) {
        this.sendEmailUseCase = sendEmailUseCase;
    }

    @PostMapping("/send")
    public Mono<ResponseEntity<Object>> sendEmail(@RequestBody Email email) {
        return sendEmailUseCase.execute(email)
                .then(Mono.just(ResponseEntity.ok().build()))
                .doOnSuccess(response -> log.info("Correo enviado exitosamente a {}", email.getTo()))
                .onErrorResume(e -> {
                    log.error("Error al enviar el correo: {}", e.getMessage(), e);
                    return Mono.just(ResponseEntity.<Void>internalServerError().build());
                });
    }
}

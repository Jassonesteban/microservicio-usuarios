package com.microservicio_usuarios.api.infrastructure.controller;

import com.microservicio_usuarios.api.application.usecase.SendEmailUseCase;
import com.microservicio_usuarios.api.domain.model.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EmailControllerTest {
    @Mock
    private SendEmailUseCase sendEmailUseCase;

    @InjectMocks
    private EmailController emailController;

    private Email email;

    @BeforeEach
    void setUp() {
            MockitoAnnotations.openMocks(this);
            email = new Email("jgualguanguzman@gmail.com", "Correo de pruebas", "Cuerpo de pruebas");

    }

    @Test
    void testSendEmailSuccess() {
        // Simular el caso de éxito
        when(sendEmailUseCase.execute(email)).thenReturn(Mono.empty());

        Mono<ResponseEntity<Object>> responseMono = emailController.sendEmail(email);

        StepVerifier.create(responseMono)
                .assertNext(response -> assertEquals(200, response.getStatusCodeValue()))
                .verifyComplete();

        verify(sendEmailUseCase, times(1)).execute(email);
    }

    @Test
    void testSendEmailFailure() {
        // Simular un error en el envío de correo
        when(sendEmailUseCase.execute(email)).thenReturn(Mono.error(new RuntimeException("Error en el servicio de email")));

        Mono<ResponseEntity<Object>> responseMono = emailController.sendEmail(email);

        StepVerifier.create(responseMono)
                .assertNext(response -> assertEquals(500, response.getStatusCodeValue()))
                .verifyComplete();

        verify(sendEmailUseCase, times(1)).execute(email);
    }
}

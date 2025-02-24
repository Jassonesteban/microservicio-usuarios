package com.microservicio_usuarios.api.infrastructure.adapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.microservicio_usuarios.api.application.port.EmailServicePort;
import com.microservicio_usuarios.api.domain.model.Email;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class EmailServiceAdapter implements EmailServicePort {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceAdapter.class);
    private final JavaMailSender mailSender;

    @Override
    public Mono<Void> sendEmail(Email email) {
        return Mono.fromRunnable(() -> {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(email.getTo());
                helper.setSubject(email.getSubject());
                helper.setText(email.getBody(), true);
                mailSender.send(message);
                log.info("Correo enviado correctamente a {}", email.getTo());
            } catch (MailAuthenticationException e) {
                log.error("Error de autenticación SMTP: Verifica tu usuario y contraseña.", e);
                throw new RuntimeException("Error de autenticación SMTP", e);
            } catch (MessagingException e) {
                log.error("Error enviando email", e);
                throw new RuntimeException("Error enviando email", e);
            }
        }).then();
    }
}

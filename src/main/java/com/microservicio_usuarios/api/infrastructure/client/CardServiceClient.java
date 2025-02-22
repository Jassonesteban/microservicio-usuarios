package com.microservicio_usuarios.api.infrastructure.client;

import com.microservicio_usuarios.api.domain.model.CardDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class CardServiceClient {

    private final WebClient webClient;

    public CardServiceClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8081/api/v1/tarjetas").build();
    }

    public Flux<CardDTO> getUserCards(List<String> cardIds) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/user-cards")
                        .queryParam("ids", String.join(",", cardIds))
                        .build())
                .retrieve()
                .bodyToFlux(CardDTO.class);
    }
}

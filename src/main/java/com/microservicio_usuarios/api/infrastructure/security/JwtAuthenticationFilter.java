package com.microservicio_usuarios.api.infrastructure.security;

import com.microservicio_usuarios.api.domain.repository.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ReactiveUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository, ReactiveUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = extractToken(exchange);

        if (token != null) {
            String username = jwtService.extractUsername(token);

            return userDetailsService.findByUsername(username)
                    .flatMap(userDetails -> {
                        if (jwtService.validateToken(token, userDetails.getUsername())) {
                            UsernamePasswordAuthenticationToken authentication =
                                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                        return chain.filter(exchange);
                    });
        }

        return chain.filter(exchange);
    }

    private String extractToken(ServerWebExchange exchange) {
        String bearerToken = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}

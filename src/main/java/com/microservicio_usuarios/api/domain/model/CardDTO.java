package com.microservicio_usuarios.api.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDTO {

    private Long id;
    private String code;
    private BigDecimal amount;
    private String name;
    private String description;
    private String company;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
}

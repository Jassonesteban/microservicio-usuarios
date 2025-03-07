package com.microservicio_usuarios.api.domain.model;

import java.util.List;

public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private List<String> cardIds;
    private Long rolId;

    public UserDTO(Long id, String name, String email, List<String> cardIds, Long rolId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.cardIds = cardIds;
        this.rolId = rolId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getCardIds() {
        return cardIds;
    }

    public void setCardIds(List<String> cardIds) {
        this.cardIds = cardIds;
    }

    public Long getRolId() {
        return rolId;
    }

    public void setRolId(Long rolId) {
        this.rolId = rolId;
    }
}

package com.microservicio_usuarios.api.domain.model;

public class AuthResponse {
    private String token;
    private String email;
    private String username;
    private Long id;
    private Long rolId;

    public AuthResponse(String token, String email, String username, Long id, Long rolId) {
        this.token = token;
        this.email = email;
        this.username = username;
        this.id = id;
        this.rolId = rolId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRolId() {
        return rolId;
    }

    public void setRolId(Long rolId) {
        this.rolId = rolId;
    }
}

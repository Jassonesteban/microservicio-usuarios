package com.microservicio_usuarios.api.infrastructure.controller;

import com.microservicio_usuarios.api.application.service.UserService;
import com.microservicio_usuarios.api.domain.model.User;
import com.microservicio_usuarios.api.domain.model.UserDTO;
import com.microservicio_usuarios.api.domain.model.UserResponseDTO;
import com.microservicio_usuarios.api.domain.repository.UserRepository;
import com.microservicio_usuarios.api.infrastructure.security.JwtService;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UserController {

    private final UserService userService;
    private final UserRepository repository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, UserRepository repository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.repository = repository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/registro")
    public Mono<ResponseEntity<UserResponseDTO>> register(@RequestBody User user) {
        return userService.register(user)
                .map(savedUser -> {
                    String token = JwtService.generateToken(savedUser.getUsername());
                    UserResponseDTO responseDTO = new UserResponseDTO(savedUser.getUsername(), savedUser.getEmail(), token);
                    return ResponseEntity.ok(responseDTO);
                });
    }

    @PutMapping("/{userId}/add-cards")
    public Mono<ResponseEntity<User>> addCardsToUser(@PathVariable Long userId, @RequestBody List<String> cardIds){
        return repository.findById(userId)
                .switchIfEmpty(Mono.error(new ChangeSetPersister.NotFoundException()))
                .flatMap(user -> {
                    user.setCardIds(new ArrayList<>(cardIds));
                    return repository.save(user);
                })
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.internalServerError().build()));
    }

    @GetMapping("/me/{userId}")
    public Mono<ResponseEntity<UserDTO>> getUserById(@PathVariable Long userId){
        return repository.findById(userId)
                .map(user -> new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getCardIds(), user.getRolId()))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{userId}")
    public Mono<ResponseEntity<Object>> deleteUser(@PathVariable Long userId){
        return repository.findById(userId)
                .flatMap(user -> repository.delete(user)
                        .then(Mono.just(ResponseEntity.noContent().build())))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PutMapping("/update/{userId}")
    public Mono<ResponseEntity<User>> updateUser(@PathVariable Long userId, @RequestBody User user){
        return repository.findById(userId)
                .switchIfEmpty(Mono.error(new ChangeSetPersister.NotFoundException()))
                .flatMap(existingUser -> {
                    if (user.getUsername() != null) {
                        existingUser.setUsername(user.getUsername());
                    }
                    if (user.getEmail() != null) {
                        existingUser.setEmail(user.getEmail());
                    }
                    return repository.save(existingUser);
                })
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.internalServerError().build()));
    }
}
